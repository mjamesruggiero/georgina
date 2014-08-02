// named because the app is Georgina;
// named because "Modal" is already defined in Bootstrap
var Gmodal = Base.extend({
    defaultOptions: {
        applyClass: 'show'
    },

    init: function ( el, options ) {
        this._super( el, options );

        var self = this, opts = this.options;

        this.$el.on(this.options.applyEvent, function() {
            self.apply();
            self.trigger(options.displayId + '-modal-apply');
        });

        this.$el.on(this.options.removeEvent, function() {
            self.apply();
            self.trigger('modal-remove');
        });
    },

    apply: function() {
        this.$el.addClass( this.options.applyClass );
    },

    remove: function() {
        this.$el.removeClass( this.options.applyClass );
    }
});
