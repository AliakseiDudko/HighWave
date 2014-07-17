App.CalculatorApp.Search = function() {
    var Search = {};

    var SearchView = Marionette.ItemView.extend({
        template: "#search-template",

        showMessage: function(message) {
            console.log(message);
        },

        initialize: function() {
            _.bindAll(this, "showMessage");
            var self = this;
            App.vent.on("search:search", function(term) {
                self.showMessage("Search " + term + ".");
            });
            App.vent.on("search:stop", function(term) {
                self.showMessage("Search finished.");
            });
            App.vent.on("search:noSearchTerm", function() {
                self.showMessage("No search term.")
            });
            App.vent.on("search:noResults", function() {
                self.showMessage("No results were found.");
            });
        },

        events: {
            "click #searchButton": "search"
        },

        search: function() {
            var searchTerm = this.$("#amountText").val().trim();
            if (searchTerm.length > 0) {
                App.vent.trigger("search:search", searchTerm);
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
        App.searchRegion.show(searchView);

        searchView.search();
    };

    return Search;
}();