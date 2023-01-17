const Panier = require("../models/panier");
const mongoose = require("mongoose");

const counterPanier = {
  id: {
    type: String,
  },
  seq: {
    type: Number,
  },
};

const countermodelpanier = mongoose.model("counterpanier", counterPanier);

const deletePanier = (req, res, next) => {
  Panier.remove()
    .then((user) => res.status(200).json(user))
    .catch((error) => res.status(400).json({ error }));
};

const getAllPanier = (req, res, next) => {
  Panier.find()
    .then((items) => res.status(200).json(items))
    .catch((error) => res.status(400).json({ error }));
};

const createPanier = (req, res, next) => {
  countermodelpanier.findOneAndUpdate(
    { id: "autoval" },
    { $inc: { seq: 1 } },
    { new: true },
    (err, cd) => {
      let seqId;
      if (cd == null) {
        const newval = new countermodelpanier({ id: "autoval", seq: 1 });
        newval.save();
        seqId = 1;
      } else {
        seqId = cd.seq;
      }

      delete req.body._id;
      const panier = new Panier({
        panierId: seqId,
        panierItemName: req.body.panierItemName,
        panierItemPrice: req.body.panierItemPrice,
      });
      panier
        .save()
        .then(() => res.status(201).json({ message: "Objet enregistré !" }))
        .catch((error) => res.status(400).json({ error }));
    }
  );
};

module.exports = {
  deletePanier,
  getAllPanier,
  createPanier
}