describe("Gmodal", function() {
  describe("toggle", function() {
    var modal = new Gmodal("#mocha");

    it("begins as hidden", function() {
      expect($("#mocha").hasClass("show")).to.be.false;
    });

    it("sets the display class when apply is set", function() {
      modal.apply();

      expect($("#mocha").hasClass("show")).to.be.true;
    });
  });
});
