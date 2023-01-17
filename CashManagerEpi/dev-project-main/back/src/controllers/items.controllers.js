const Items = require("../models/items");
const mongoose = require("mongoose");

const counterItems = {
  id: {
    type: String,
  },
  seq: {
    type: Number,
  },
};
const countermodelitems = mongoose.model("counteritems", counterItems);
const getAllItems = (req, res, next) => {
  Items.find()
    .then((items) => res.status(200).json(items))
    .catch((error) => res.status(400).json({ error }));
};

const getItemCodeBarre = (req, res, next) => {
  Items.findOne({ itemCodebarre: req.params.itemCodebarre })
    .then((items) => res.status(200).json(items))
    .catch((error) => res.status(404).json({ error }));
};

const createItem = (req, res, next) => {
  countermodelitems.findOneAndUpdate(
    { id: "autoval" },
    { $inc: { seq: 1 } },
    { new: true },
    (err, cd) => {
      let seqId;
      if (cd == null) {
        const newval = new countermodelitems({ id: "autoval", seq: 1 });
        newval.save();
        seqId = 1;
      } else {
        seqId = cd.seq;
      }

      delete req.body._id;
      const items = new Items({
        itemId: seqId,
        itemName: req.body.itemName,
        itemPrice: req.body.itemPrice,
        itemCodebarre: req.body.itemCodebarre,
      });
      items
        .save()
        .then(() => res.status(201).json({ message: "Objet enregistré !" }))
        .catch((error) => res.status(400).json({ error }));
    }
  );
};

module.exports = {
    getAllItems,
    getItemCodeBarre,
    createItem
}