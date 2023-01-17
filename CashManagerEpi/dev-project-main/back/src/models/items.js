const mongoose = require('mongoose');

const itemsSchema = mongoose.Schema({
    itemName: {type: String, required: true},
    itemId: {type: String, required: true},
    itemPrice: {type: String, required: true},
    itemCodebarre:{ type: Number, required:true}
});

module.exports = mongoose.model('items', itemsSchema);