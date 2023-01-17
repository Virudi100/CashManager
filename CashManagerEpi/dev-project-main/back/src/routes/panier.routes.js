const express = require("express");
const router = express.Router();

const {
    deletePanier,
    getAllPanier,
    createPanier
  } = require("../controllers/panier.controllers");


router.post("/panier/createPanier/", createPanier) 

router.get("/panier/getAllPanier/", getAllPanier) //ok

router.delete("/panier/deletePanier", deletePanier)

module.exports = router