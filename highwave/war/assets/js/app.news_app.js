App.NewsApp = function() {
    var NewsApp = {};

    var News = Backbone.Model.extend({
        defaults: {
            user: {
                screenName: ""
            },
            mediaEntities: [{
                mediaURL: ""
            }]
        }
    });

    var NewsFeed = Backbone.Collection.extend({
        model: News,

        initialize: function() {
            var self = this;

            _.bindAll(this, "load");
            App.vent.on("news:load", function() {
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
                }
                else {
                    App.vent.trigger("news:noResults");
                }
            });
        },

        fetchNews: function(callback) {
            if (this.loading) {
                return;
            }

            this.loading = true;

            var self = this;
            App.vent.trigger("news:fetch:start");

            $.ajax({
                url: "https://high-wave-595.appspot.com/_ah/api/deposits/v0/news",
                success: function(res) {
                    App.vent.trigger("news:fetch:stop");
                    if (res.items.length == 0) {
                        callback([]);
                        return [];
                    }
                    if (res.items) {
                        var newsResults = [];
                        _.each(res.items, function(item) {
                            var tweet = item.retweetedStatus !== undefined ? item.retweetedStatus : item;
                            newsResults[newsResults.length] = new News(tweet);
                        });
                        callback(newsResults);
                        self.loading = false;
                        return newsResults;
                    }
                    else if (res.error) {
                        App.vent.trigger("news:error");
                        self.loading = false;
                    }
                    return null;
                }
            });
        }
    });

    NewsApp.NewsFeed = new NewsFeed();

    return NewsApp;
}();

App.addInitializer(function() {
    App.NewsApp.NewsList.showNews(App.NewsApp.NewsFeed);
});