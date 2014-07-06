App = new Marionette.Application();

App.addRegions({
	searchRegion : "#search-region"
});

App.SearchView = Marionette.ItemView.extend({
	template : "#search-template"
});

App.on("start", function() {
	var searchView = new App.SearchView();
	App.searchRegion.show(searchView);
});

App.start();




MyApp = new Marionette.Application();

var ModalRegion = Backbone.Marionette.Region.extend({
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

MyApp.addRegions({
    content : "#results-region",
    modal : ModalRegion
});

$(document).ready(function() {
	MyApp.start();
});