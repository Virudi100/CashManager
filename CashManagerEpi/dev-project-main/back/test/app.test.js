const mongoose = require("mongoose");
const request = require("supertest");
const app= require("../src/app");
const User = require("../src/models/user");





describe("POST /users", () => {

    describe("when passed a username and password", () => {
        test("should respond with a 200 status code", async () => {
           console.log("salut")
        })
    })

})
