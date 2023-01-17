const express = require("express");
const router = express.Router();

const {
  createBank,
  getAllBanks,
  getBankId,
  updateBank,
  bankPayment
} = require("../controllers/bank.controllers");

router.get("/bank/getAllBank", getAllBanks)

router.get("/bank/payment/:cardId", bankPayment)

router.get("/bank/:bankId", getBankId)

router.put("/bank/:cardId", updateBank)

router.post("/bank/createBank", createBank)


module.exports = router