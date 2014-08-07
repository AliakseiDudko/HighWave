App.NewsApp = function() {
    var NewsApp = {};

    var News = Backbone.Model.extend({});

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
                } else {
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

            gapi.client.deposits.get.news.feed().execute(function(res) {
                App.vent.trigger("news:fetch:stop");
                if (res.error) {
                    App.vent.trigger("news:error");
                    callback([]);
                    self.loading = false;
                }
                if (res.items) {
                    var newsResults = [];
                    _.each(res.items, function(item) {
                        newsResults[newsResults.length] = new News(item);
                    });
                    callback(newsResults);
                    self.loading = false;
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