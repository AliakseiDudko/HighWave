define([ "backbone" ], function(Backbone) {
    var Deposit = Backbone.Model.extend();

    var Deposits = Backbone.Collection.extend({
        model: Deposit,

        initialize: function() {
            var self = this;

            _.bindAll(this, "search");
            Backbone.Events.on("search:searchQuery", function(query) {
                self.search(query);
            });

            this.loading = false;
        },

        search: function(searchQuery) {
            var self = this;

            self.reset([]);

            this.fetchDeposits(searchQuery, function(deposits) {
                if (deposits.length > 0) {
                    self.reset(deposits);
                } else {
                    App.vent.trigger("app:logMessage", "No results were found.");
                }
            });
        },

        fetchDeposits: function(searchQuery, callback) {
            if (this.loading) {
                return;
            }

            this.loading = true;

            var self = this;
            Backbone.Events.trigger("app:logMessage", "Search started.");

            gapi.client.deposits.get.deposits.list(searchQuery).execute(function(res) {
                Backbone.Events.trigger("app:logMessage", "Search finished.");

                self.loading = false;
                var searchResults = [];

                if (res.error) {
                    Backbone.Events.trigger("app:logMessage", "Error, please retry later.");
                } else if (res.items) {
                    _.each(res.items, function(item) {
                        searchResults[searchResults.length] = new Deposit(item);
                    });
                }
                callback(searchResults);
            });
        }
    });

    return Deposits;
});