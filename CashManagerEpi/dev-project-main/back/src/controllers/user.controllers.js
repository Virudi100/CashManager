const User = require("../models/user");
const bcrypt = require("bcryptjs");
const jwt = require("jwt-simple");
const express = require("express");
const app = express();
const mongoose = require("mongoose");

var hashedPassword;
const moment = require("moment");
app.set("jwtTokenSecret", "Hugo");

const counterUser = {
  id: {
    type: String,
  },
  seq: {
    type: Number,
  },
};
const countermodel = mongoose.model("counteruser", counterUser);

const register = (req, res, next) => {
  User.findOne({ email: req.body.email }, function (err, user) {
    console.log(!user);
    if (user) {
      const responseData = {
        message: "email exitant",
      };
      const jsonContent = JSON.stringify(responseData);
      res.end(jsonContent);
    } else {
      countermodel.findOneAndUpdate(
        { id: "autoval" },
        { $inc: { seq: 1 } },
        { new: true },
        (err, cd) => {
          let seqId;
          if (cd == null) {
            const newval = new countermodel({ id: "autoval", seq: 1 });
            newval.save();
            seqId = 1;
          } else {
            seqId = cd.seq;
          }
          delete req.body._id;
          const user = new User({
            password: req.body.password,
            email: req.body.email,
            username: req.body.username,
            userId: seqId,
          });
          user
            .save()
            .then(() => res.status(201).json({ message: "Objet enregistré !" }))
            .catch((error) => res.status(400).json({ error }));
        }
      );
    }
  });
};
const login = (req, res, next) => {
  User.findOne({ email: req.body.email }, function (err, user) {
    if (err) {
      // user not found
      return res.send(401);
    }

    if (!user) {
      // incorrect email
      return res.send(400);
    }
    // Comparing the original password to
    // encrypted password
    console.log(user);
    console.log(user.password);
    console.log(req.body.password);
    if (user.password == req.body.password) {
      // User has authenticated OK
      let expires = moment().add("days", 7).valueOf();
      let token = jwt.encode(
        {
          iss: user.userId,
          exp: expires,
        },
        app.get("jwtTokenSecret")
      );
      console.log(user);
      res.json({
        token: token,
        expires: expires,
        user: user.toJSON(),
      });
    } else {
      // If password doesn't match the following
      // message will be sent
      console.log("password not ok");
      res.send(401);
    }
  });
};
const getAllUsers = (req, res, next) => {
  User.find()
    .then((user) => res.status(200).json(user))
    .catch((error) => res.status(400).json({ error }));
  console.log(res);
};

const deleteUser = (req, res, next) => {
  User.findOneAndDelete({ userId: req.params.userId })
    .then((user) => res.status(200).json(user))
    .catch((error) => res.status(400).json({ error }));
};

const updateUser = (req, res, next) => {
  console.log("mmmmmm");
  User.findOne({ userId: req.params.userId }, function (err, user) {
    // todo: don't forget to handle err

    if (!user) {
      res.end("No account found");
    }
    console.log(req.body.password);
    console.log(req.body.username);
    let email = req.body.email;
    let username = req.body.username;
    let password = req.body.password;

    // validate
    if (!email || !username || !password) {
      // simplified: '' is a falsey
      res.end("One or more fields are empty");
    }

    bcrypt.genSalt(10, function (err, Salt) {
      // The bcrypt is used for encrypting password.
      bcrypt.hash(req.body.password, Salt, function (err, hash) {
        if (err) {
          return console.log("Cannot encrypt");
        }

        var hashedPassword = hash;
        console.log(hashedPassword);
        user.password = hash;
      });
      // no need for else since you are returning early ^
      user.email = req.body.email;
      user.username = req.body.username;
      user.userId = req.params.userId;

      // don't forget to save!
      user.save().then((user) => res.status(200).json(user));
    });
  });
};

const getUserId = (req, res, next) => {
  User.findOne({ userId: req.params.userId })
    .then((user) =>
      res.status(200).json({
        userId: user.userId,
        password: user.password,
        email: user.email,
        username: user.username,
      })
    )
    .catch((error) => res.status(404).json({ error }));
};

module.exports = {
  register,
  login,
  getAllUsers,
  deleteUser,
  updateUser,
  getUserId,
};
