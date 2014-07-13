App.NewsApp.NewsList = function() {
    var NewsList = {};

    var NewsView = Marionette.ItemView.extend({
        template: "#news-template"
    });

    var NewsListEmptyView = Marionette.ItemView.extend({
        template: "#news-list-empty-template"
    });

    var NewsListView = Marionette.CompositeView.extend({
        template: "#news-list-template",
        childView: NewsView,
        emptyView: NewsListEmptyView,

        showMessage: function(message) {
            console.log("message");
        },

        initialize: function() {
            _.bindAll(this, "showMessage");
            var self = this;
            App.vent.on("news:error", function() {
                self.showMessage("Error, please retry later :s");
            });
            App.vent.on("news:noResults", function() {
                self.showMessage("No news found")
            });
        }
    });

    NewsList.showNews = function(news) {
        var newsListView = new NewsListView({
            collection: news
        });
        App.newsRegion.show(newsListView);
        
        App.vent.trigger("news:load");
    };

    return NewsList;
}();

App.vent.on("news:rendered", function() {
    App.NewsApp.NewsList.showNews(App.NewsApp.NewsFeed);
});