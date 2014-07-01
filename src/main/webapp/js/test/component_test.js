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

  describe("templatedStrings", function() {
    it("returns array of templated strings", function() {
      var expected = ["<td>foo</td>", "<td>bar</td>"];
      var templ = "<td><%- category %></td>";
      var rows = [{category: "foo"}, {category: "bar"}];
      var returned = Components.templatedStrings(templ, rows);

      expect(returned[0]).to.equal(expected[0]);
      expect(returned[1]).to.equal(expected[1]);
      expect(returned.length).to.equal(expected.length);
    });
  });

  describe("existy", function() {
    it("returns true for 'existy' things", function() {
        var returned = [1, "defined", false].map(Components.existy);
        expect(_.uniq(returned)[0]).to.be.true;
    });

    it("returns false for things that are not 'existy'", function() {
        var returned = [null, undefined, false].map(Components.existy);
        expect(_.uniq(returned)[0]).to.be.false;
    });
  });

  describe("truthy", function() {
    it("returns true for 'truthy' things", function() {
        var returned = [1, "defined", 0, ""].map(Components.truthy);
        expect(_.uniq(returned)[0]).to.be.true;
    });

    it("returns false for things that are not 'truthy'", function() {
        var returned = [null, undefined, false].map(Components.truthy);
        expect(_.uniq(returned)[0]).to.be.false;
    });
  });

  describe("cat", function() {
    it("returns a concatenated array from the args offered", function(){
        var returned = Components.cat([1, 2, 3], [4, 5], [6, 7, 8]);
        expect(returned).to.eql([1, 2, 3, 4, 5, 6, 7, 8]);
    });
  });

  describe("construct", function() {
      it("conses an element to the head of an array", function() {
          var returned = Components.construct(55, [9, 8, 7]);
          expect(returned).to.eql([55, 9, 8, 7]);
      });
  });

  describe("mapcat", function() {
      it("calls a function for every element in an array", function() {
          var returned = Components.mapcat(function(e) {
              return Components.construct(e, [","]);
          }, [1, 2, 3]);
          expect(returned).to.eql([1, ",", 2, ",", 3, ","]);
      });
  });

  describe("butLast", function() {
      it("returns all but the last element of a collection", function(){
          var returned = Components.butLast([1, 2, 3, 4, 5]);
          expect(returned).to.eql([1, 2, 3, 4]);
      });
  });

  describe("interpose", function() {
    it("slips an element between existing elements", function() {
        var returned = Components.interpose("|", [1, 3, 4]);
        expect(returned).to.eql([1, "|", 3, "|",  4]);
    });
  });
});
