define([ "marionette", "regions/modal", "models/deposits", "models/search", "models/newsFeed", "views/depositDetails", "views/depositList",
        "views/search", "views/newsFeed" ], function(Marionette, ModalRegion, Deposits, SearchModel, NewsFeed, DepositDetailsView,
        DepositListView, SearchView, NewsFeedView) {
    var App = new Marionette.Application();

    App.addRegions({
        searchRegion: "#search-region",
        resultsRegion: "#results-region",
        newsRegion: "#news-region",
        modalRegion: ModalRegion
    });

    App.addInitializer(function() {
        var self = this;

        Backbone.Events.on("app:logMessage", function(message) {
            self.showMessage(message);
        });
        Backbone.Events.on("app:showDepositDetails", function(details) {
            self.showDepositDetails(details);
        });

        $("#search-panel").find(".panel-heading").find(".panel-title").text(Globalize.translate("panelHeaders/search"));
        $("#results-panel").find(".panel-heading").find(".panel-title").text(Globalize.translate("panelHeaders/deposits"));
        $("#news-panel").find(".panel-heading").find(".panel-title").text(Globalize.translate("panelHeaders/twitter"));

        var depositListView = new DepositListView({
            collection: new Deposits()
        });
        this.resultsRegion.show(depositListView);

        var searchView = new SearchView({
            model: new SearchModel()
        });
        this.searchRegion.show(searchView);

        var newsFeedView = new NewsFeedView({
            collection: new NewsFeed()
        });
        this.newsRegion.show(newsFeedView);
    });

    App.showMessage = function(message) {
        console.log(message);
    };

    App.showDepositDetails = function(details) {
        var detailsView = new DepositDetailsView({
            model: details
        });
        this.modalRegion.show(detailsView);
    };

    return App;
});