define([ "backbone" ], function(Backbone) {
    var Deposit = Backbone.Model.extend();

    var Deposits = Backbone.Collection.extend({
        model: Deposit,

        showMessage: function(message) {
            console.log(message);
        },

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
                    App.vent.trigger("search:noResults");
                }
            });
        },

        fetchDeposits: function(searchQuery, callback) {
            if (this.loading) {
                return;
            }

            this.loading = true;

            var self = this;
            Backbone.Events.trigger("search:start");

            gapi.client.deposits.get.deposits.list(searchQuery).execute(function(res) {
                Backbone.Events.trigger("search:stop");
                if (res.error) {
                    Backbone.Events.trigger("search:error");
                    callback([]);
                    self.loading = false;
                }
                if (res.items) {
                    var searchResults = [];
                    _.each(res.items, function(item) {
                        searchResults[searchResults.length] = new Deposit(item);
                    });
                    callback(searchResults);
                    self.loading = false;
                }
            });
        }
    });

    return Deposits;
});