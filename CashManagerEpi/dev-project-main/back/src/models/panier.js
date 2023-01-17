const mongoose = require('mongoose');

const panierSchema = mongoose.Schema({
    panierId: {type: String, required: true},
    panierItemName: {type: String, required: true},
    panierItemPrice: {type: Number, required: true},
});

module.exports = mongoose.model('panier', panierSchema);