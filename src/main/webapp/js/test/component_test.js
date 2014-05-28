var expect = chai.expect;

describe("Components", function() {
  describe("numberWithCommas", function() {
    it("should add commas to a six-figured number", function() {
      var number = 123456;
      expect(Components.numberWithCommas(number)).to.equal("123,456");
    });
  });
});
