const Bank = require("../models/bank");

const createBank = (req, res, next) => {
  Bank.findOne({ cardId: req.body.cardId }, function (err, bank) {
    if (bank) {
      const responseData = {
        message: "carte exitante",
      };
      const jsonContent = JSON.stringify(responseData);
      res.end(jsonContent);
    } else {
      const bank = new Bank({
        montant: req.body.montant,
        cardId: req.body.cardId,
        userId: req.body.userId,
      });
      bank
        .save()
        .then(() => res.status(201).json({ message: "Objet enregistré !" }))
        .catch((error) => res.status(400).json({ error }));
    }
  });
};

const getAllBanks = (req, res, next) => {
  Bank.find()
    .then((user) => res.status(200).json(user))
    .catch((error) => res.status(400).json({ error }));
};

const getBankId = (req, res, next) => {
  Bank.findOne({ userId: req.params.bankId })
    .then((user) => res.status(200).json(user))
    .catch((error) => res.status(404).json({ error }));
};

const updateBank = (req, res, next) => {
  console.log("mmmmmm");
  Bank.findOne({ cardId: req.params.cardId }, function (err, bank) {
    console.log(bank.cardId);
    // todo: don't forget to handle err

    if (!bank) {
      res.end("No account found");
    }

    let montant = req.body.montant;

    // validate
    if (!montant) {
      // simplified: '' is a falsey
      res.end("One or more fields are empty");
    }
    // no need for else since you are returning early ^
    bank.montant = montant;

    // don't forget to save!
    bank.save().then((user) => res.status(200).json(user));
  });
};

const bankPayment = (req, res, next) => {
  Bank.findOne({ cardId: req.params.cardId })
    .then((bank) => res.status(200).json(bank))
    .catch((error) => res.status(404).json({ error }));
};

module.exports = {
  createBank,
  getAllBanks,
  getBankId,
  updateBank,
  bankPayment,
};
