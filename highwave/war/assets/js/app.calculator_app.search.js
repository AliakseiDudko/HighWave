App.CalculatorApp.Search = function() {
    var Search = {};

    var SearchView = Marionette.ItemView.extend({
        template: "#search-template",

        initialize: function() {
            var self = this;
            App.vent.on("search:start", function() {});
            App.vent.on("search:stop", function() {});
            App.vent.on("search:term", function() {});
        },

        events: {
            "click #searchButton": "search"
        },

        search: function() {
            var searchTerm = this.$("#amountText").val().trim();
            if (searchTerm.length > 0) {
                App.vent.trigger("search:term", searchTerm);
            }
            else {
                App.vent.trigger("search:noSearchTerm", searchTerm);
            }
        }
    });

    Search.showSearch = function(search) {
        var searchView = new SearchView({
            model: search
        });
        App.CalculatorApp.layout.search.show(searchView);

        App.vent.trigger("search:term");
    };

    return Search;
}();

App.vent.on("layout:rendered", function() {
    App.CalculatorApp.Search.showSearch(App.CalculatorApp.SearchModel);
});