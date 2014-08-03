App = new Marionette.Application();

var ModalRegion = Marionette.Region.extend({
    el: "#modal-region",

    constructor: function() {
        _.bindAll(this, "showModal", "hideModal");
        Backbone.Marionette.Region.prototype.constructor.apply(this, arguments);
        this.on("show", this.showModal, this);
    },

    getEl: function(selector) {
        var $el = $(selector);
        $el.on("hidden", this.close);
        return $el;
    },

    showModal: function(view) {
        view.on("close", this.hideModal, this);
        this.$el.modal("show");
    },

    hideModal: function() {
        this.$el.modal("hide");
    }
});

App.addRegions({
    searchRegion: "#search-region",
    resultsRegion: "#results-region",
    calculatorRegion: "#calculator-region",
    newsRegion: "#news-region",
    modalRegion: ModalRegion
});

function initGoogleApi() {
    gapi.client.load("deposits", "v0", function() {
        App.start();
    }, "https://high-wave-595.appspot.com/_ah/api");
}