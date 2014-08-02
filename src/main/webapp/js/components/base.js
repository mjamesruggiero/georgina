Base = Class.extend({
    defaultOptions: {},

    init: function ( el, options ) {
        // auto-assign jQuery object
        this.$el = $( el );

        // options merge
        this.options = $.extend( {}, this.defaultOptions, options );
    },

    // event publishing/subscription
    trigger: function ( evt, data ) {
        $( this ).trigger( evt, data );
    },

    on: function ( evt, fn ) {
        $( this ).on( evt, fn );
    }
});

//// Custom extend does an automatic extend of defaultOptions
//Base.extend = function ( proto ) {
    //proto.defaultOptions = $.extend( {}, this.prototype.defaultOptions, proto.defaultOptions );
    //var newClass = this._extend( proto );
    //newClass.extend = Base.extend;
    //return newClass;
//};
