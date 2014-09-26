define([ "marionette", "text!templates/news-feed-empty-template.html", "text!templates/news-feed-template.html" ], function(Marionette,
        emptyTemplateHtml, templateHtml) {
    var NewsView = Marionette.ItemView.extend({
        render: function() {
            this.$el.append(this.model.get("html"));
        }
    });

    var NewsFeedEmptyView = Marionette.ItemView.extend({
        template: _.template(emptyTemplateHtml),
        className: "panel-body"
    });

    var NewsFeedView = Marionette.CollectionView.extend({
        template: _.template(templateHtml),
        childView: NewsView,
        emptyView: NewsFeedEmptyView
    });

    return NewsFeedView;
});