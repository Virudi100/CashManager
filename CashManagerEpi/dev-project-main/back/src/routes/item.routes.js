const express = require("express");
const router = express.Router();

const {
  getAllItems,
  getItemCodeBarre,
  createItem
} = require("../controllers/items.controllers");


router.post("/item/createItem/", createItem) 

router.get("/item/getAllItems/", getAllItems) //ok

router.get("/item/:itemCodebarre", getItemCodeBarre)

module.exports = router