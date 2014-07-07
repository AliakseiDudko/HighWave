App = new Marionette.Application();

var ModalRegion = Marionette.Region.extend({
    el : "#modal",

    constructor : function() {
	    _.bindAll(this, "showModal", "hideModal");
	    Backbone.Marionette.Region.prototype.constructor.apply(this, arguments);
	    this.on("show", this.showModal, this);
    },

    getEl : function(selector) {
	    var $el = $(selector);
	    $el.on("hidden", this.close);
	    return $el;
    },

    showModal : function(view) {
	    view.on("close", this.hideModal, this);
	    this.$el.modal("show");
    },

    hideModal : function() {
	    this.$el.modal("hide");
    }
});

App.addRegions({
    calculatorRegion : "#calculator-region",
    modalRegion : ModalRegion
});

$(document).ready(function() {
	App.start();
});
