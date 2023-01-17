const express = require("express");
const router = express.Router();

const {
  register,
  login,
  getAllUsers,
  deleteUser,
  updateUser,
  getUserId,
} = require("../controllers/user.controllers");

router.post("/user/register", register); //ok
router.post("/user/login", login); // ok
router.get("/user/getAllUsers", getAllUsers); //ok
router.get("/user/:userId", getUserId); //ok
router.put("/user/:userId", updateUser);
router.delete("/user/:userId", deleteUser);

module.exports = router;
