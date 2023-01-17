const { config } = require("dotenv");
config();

const express = require("express");
const app = express();
const mongoose = require("mongoose");
const bcrypt = require("bcryptjs");
const jwt = require("jwt-simple");
var hashedPassword;
const moment = require("moment");

app.set("jwtTokenSecret", "Hugo");

const Bank = require("./models/bank");
const User = require("./models/user");
const user = require("./models/user");
const bank = require("./models/bank");
const Items = require("./models/items");
const items = require("./models/items");
const panier = require("./models/panier");

app.use(express.json());

app.use((req, res, next) => {
  res.setHeader("Access-Control-Allow-Origin", "*");
  res.setHeader(
    "Access-Control-Allow-Headers",
    "Origin, X-Requested-With, Content, Accept, Content-Type, Authorization"
  );
  res.setHeader(
    "Access-Control-Allow-Methods",
    "GET, POST, PUT, DELETE, PATCH, OPTIONS"
  );
  next();
});

mongoose
  .connect(process.env.DATABASE, console.log(process.env.DATABASE), {
    useNewUrlParser: true,
    useUnifiedTopology: true,
  })
  .then(() => console.log("Connexion à MongoDB réussie !"))
  .catch(() => console.log("Connexion à MongoDB échouée !"));

const bankRoutes = require("./routes/bank.routes");
const userRoutes = require("./routes/user.routes");
const itemRoutes = require("./routes/item.routes");
const panierRoutes = require("./routes/panier.routes");
app.use("/api/", bankRoutes);
app.use("/api/", userRoutes);
app.use("/api/", itemRoutes);
app.use("/api/", panierRoutes)


module.exports = app;
