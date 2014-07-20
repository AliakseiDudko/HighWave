App.CalculatorApp.Search = function() {
    var Search = {};

    var SearchView = Marionette.ItemView.extend({
        template: Backbone.Marionette.TemplateCache.get("search-template"),

        showMessage: function(message) {
            console.log(message);
        },

        initialize: function() {
            _.bindAll(this, "showMessage");
            var self = this;
            App.vent.on("search:searchQuery", function(query) {
                self.showMessage("Search " + query + ".");
            });
            App.vent.on("search:stop", function() {
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
            var amount = this.$("#amountText").val().trim();
            var period = this.$("#periodText").val().trim();

            if (amount.length !== 0 && period.length !== 0) {
                var searchQuery = "amount=" + amount + "&period=" + period;
                App.vent.trigger("search:searchQuery", searchQuery);
            } else {
                App.vent.trigger("search:noSearchQuery");
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