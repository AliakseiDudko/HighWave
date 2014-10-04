define([ "backbone" ], function(Backbone) {
    var News = Backbone.Model.extend({});

    var NewsFeed = Backbone.Collection.extend({
        model: News,

        initialize: function() {
            var self = this;

            Backbone.Events.on("gapi:loaded", function() {
                self.load();
            });

            this.loading = false;
        },

        load: function() {
            var self = this;

            self.reset([]);

            this.fetchNews(function(news) {
                if (news.length > 0) {
                    self.reset(news);
                } else {
                    Backbone.Events.trigger("app:logMessage", "No news were found.");
                }
            });
        },

        fetchNews: function(callback) {
            if (this.loading) {
                return;
            }

            this.loading = true;

            var self = this;
            Backbone.Events.trigger("app:logMessage", "Loading news started.");

            gapi.client.deposits.get.news.feed().execute(function(res) {
                Backbone.Events.trigger("app:logMessage", "Loading news finished.");

                self.loading = false;
                var newsResults = [];

                if (res.error) {
                    Backbone.Events.trigger("app:logMessage", "Error, please retry later.");
                }
                if (res.items) {
                    _.each(res.items, function(item) {
                        newsResults[newsResults.length] = new News(item);
                    });
                }
                callback(newsResults);
            });
        }
    });

    return NewsFeed;
});