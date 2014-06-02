var expect = chai.expect;

describe("Components", function() {
  describe("numberWithCommas", function() {
    it("adds commas to a six-figured number", function() {
      var number = 123456;
      expect(Components.numberWithCommas(number)).to.equal("123,456");
    });

    it("doesn't do anything to a three-figured number", function() {
      var number = 123;
      expect(Components.numberWithCommas(number)).to.equal("123");
    });

    it("handles floats", function() {
      var number = 123456.789;
      expect(Components.numberWithCommas(number)).to.equal("123,456.789");
    });
  });

  describe("getDateSpans", function() {
    it("returns a default startDate", function() {
      var expected = "2013-01-01";
      var spans = Components.getDateSpans();
      expect(spans.startDate).to.equal(expected);
    });

    it("returns today as default endDate", function() {
      var expected = Components.getCurrentFormattedDate();
      var spans = Components.getDateSpans();
      expect(spans.endDate).to.equal(expected);
    });
  });

  describe("getCurrentFormattedDate", function() {
    var sandbox,
        unixTimestampForMay29 = 1401324497313;

    beforeEach(function() {
        sandbox = sinon.sandbox.create();
        sandbox.clock = sinon.useFakeTimers(unixTimestampForMay29);
    });

    afterEach(function() {
        sandbox.restore();
    });

    it("returns a the current date, formatted", function() {
      var expected = "2014-05-29";
      var spans = Components.getDateSpans();
      expect(spans.endDate).to.equal(expected);
    });
  });
});
