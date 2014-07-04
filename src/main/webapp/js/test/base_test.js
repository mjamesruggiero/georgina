describe("Base", function() {
  describe("init", function() {
    it("sets the class element", function() {
        var b = new Base('#mocha');
        expect(b.$el.length).to.equal(1);
    });

    it("sets options", function() {
        var b = new Base('#mocha', { 'monkey': 'capuchin' });
        expect(b.options.monkey).to.equal('capuchin');
    });
  });
});
