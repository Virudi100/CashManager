const mongoose = require('mongoose');

const bankSchema = mongoose.Schema({
    montant: {type: Number, required: true},
    cardId: {type: String, required: true},
    userId: {type: String, required: true}
});

module.exports = mongoose.model('bank', bankSchema);