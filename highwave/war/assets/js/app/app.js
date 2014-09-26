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

        _.bindAll(this, "showMessage");

        Backbone.Events.on("search:searchQuery", function() {
            self.showMessage("Search started.");
        });
        Backbone.Events.on("search:stop", function() {
            self.showMessage("Search finished.");
        });
        Backbone.Events.on("search:noSearchTerm", function() {
            self.showMessage("Please enter amount and period.");
        });
        Backbone.Events.on("search:noResults", function() {
            self.showMessage("No results were found.");
        });
        Backbone.Events.on("search:error", function() {
            self.showMessage("Error, please retry later.");
        });
        Backbone.Events.on("news:error", function() {
            self.showMessage("Error, please retry later :s");
        });
        Backbone.Events.on("news:noResults", function() {
            self.showMessage("No news found");
        });
        Backbone.Events.on("news:fetch:start", function() {
            self.showMessage("Loading news started.");
        });
        Backbone.Events.on("news:fetch:stop", function() {
            self.showMessage("Loading news finished.");
        });

        Backbone.Events.on("app:load", function() {
            self.showDeposits(new Deposits());
            self.showSearch(new SearchModel());
            self.showNews(new NewsFeed());
        });
        Backbone.Events.on("app:showDepositDetails", function(details) {
            self.showDepositDetails(details);
        });
    });

    App.showMessage = function(message) {
        console.log(message);
    };

    App.showDeposits = function(deposits) {
        var depositListView = new DepositListView({
            collection: deposits
        });
        this.resultsRegion.show(depositListView);
    };

    App.showSearch = function(search) {
        var searchView = new SearchView({
            model: search
        });
        this.searchRegion.show(searchView);

        searchView.search();
    };

    App.showNews = function(news) {
        var newsFeedView = new NewsFeedView({
            collection: news
        });
        this.newsRegion.show(newsFeedView);

        news.load();
    };

    App.showDepositDetails = function(details) {
        var detailsView = new DepositDetailsView({
            model: details
        });
        this.modalRegion.show(detailsView);
    }

    return App;
});