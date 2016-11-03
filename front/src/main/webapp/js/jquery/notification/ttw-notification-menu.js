 /*
 * Created by 23rd and Walnut for Codebasehero.com
 * www.23andwalnut.com
 * www.codebasehero.com
 * User: Saleem El-Amin
 * Date: 7/27/11
 * Time: 6:41 AM
 *
 * Version: 1.00
 * License: You are free to use this file in personal and commercial products, however re-distribution 'as-is' without prior consent is prohibited.
 */


(function($) {


     $.ttwNotificationMenu = function(userOptions) {
        var defaults,
                options,
                markup,
                notificationMenu = this,
                notifications = {},
                menuItems = {},
            //tracks whether there is an increase or decrease in the notification count for a category
                menuItemPrevCount = {},
                MenuItem,
                Notification,
                NotificationList,

                categories,
                types,
                cssSelector,
                notificationList,
                colorIndex = 0;


        defaults = {
            notificationList:{
                showMenu:true,
                anchor:'bubble',
                offset: '0 24'
            },
            bubble:{
                useColors:true,
                colors:['#f56c7e', '#fec151', '#7ad2f4'],
                showEmptyBubble:true
            },
            language: {
            	unread:'Unread',
            	all:'All'
            },
            showNotificationList:true,
            notificationListEmptyText:'No Notifications',
            defaultCategory : 'general',
            anchor:'body',
            relativeImagePath:'notification_menu/images/',
            createCallback:function(notification){
            },
            deleteCallback:function(notification){
            },
            notificationCountIncrease:function(notification){
            },
            notificationClickCallback:function(notification){
            }
        };

        markup = {
            tmp:'<div id="tmp"></div>',
            notification:'<div class="ttw-notification">' +
                    '<span class="icon"></span>' +
                    '<span class="message"></span>' +
                    '<span class="close"></span>' +
                    '</div>',
            notificationIcon:'<img src="" alt="notification icon"/>',
            notificationBubble:'<span class="notification-bubble" title="Notifications"></span>'
        };

        cssSelector = {
            notification:'.ttw-notification',
            notificationList:'.notification-list',
            notificationListItem:'.notification-list-item',
            notificationMessage:'.message',
            notificationClose:'.close',
            notificationListMenuItem:'.notification-list-menu-item',
            notificationListCloseButton:'.close-notification-list',
            notificationIcon:'.icon'
        };

        categories = [];

        //Internal Methods
        function init() {
             options = $.extend(true, {}, defaults, userOptions);

             createCategory(options.defaultCategory);
         }

        function createCategory(category, menuItem) {
            categories.push(category);

            notifications[category] = {};

            notifications[category].count = 0;
            notifications[category].readCount = 0;
            notifications[category].unreadCount = 0;

            notifications[category].read = {};        //create an empty notifications array for the category
            notifications[category].unread = {};        //create an empty notifications array for the category

            if (menuItem && (category != options.defaultCategory))
                menuItems[category] = menuItem;     //store a reference to the specific menu item for this category
        }

        function registerNotification(notification) {
            // var category = (notification.category) ? notification.category : options.defaultCategory;
            var category = notification.category;

            if (!notifications[category])
                createCategory(category);

            notifications[category].count++;

            //add the notification in the appropriate read/unread bucket
            if (!notification.read) {
                notifications[category].unreadCount++;
                notifications[category].unread[notification.id] = notification;
            }
            else {
                notifications[category].readCount++;
                notifications[category].read[notification.id] = notification;
            }

            //Determine if this notification is a type that should update a bubble, update the appropriate bubble if it is.
            if (logInMenu(category, notification))
                menuItems[category].updateBubble();
        }

        function logInMenu(category, notification) {
            //TODO: combine with update bubble
            //condition 1: Ensure there is a menu item associated with this category
            //condition 2: since the default category isn't associated with a menu item, there is no bubble

            return (typeof menuItems[category] != 'undefined') && (category != options.defaultCategory)
        }

        function unregisterNotification(notification) {
            //We need the textual representation of the read status to access it in the notifications object
            var read_status = notification.read ? 'read' : 'unread';
            delete notifications[notification.category][read_status][notification.id];
            delete notification;
        }

        function isValidCategory(category) {
            //make sure the category is defined and was previously registered
            return category && notifications[category];
        }

        function isValidReadStatus(type) {
            return ($.inArray(type, ['read', 'unread', 'all']) != -1);
        }

        function uniqueId(prefix) {
            //getRandomInt from:https://developer.mozilla.org/en/JavaScript/Reference/Global_Objects/Math/random
            return  ((typeof prefix != 'undefined') ? prefix : '') + new Date().getTime() + '-' + Math.floor(Math.random() * (100000 - 1 + 1)) + 1;
        }

        function getNotifications(category, readStatus) {

            if (!readStatus)
                readStatus = 'unread';

            //all is the only 'non-valid' category that is allowed. Exit the function if supplied cat isn't valid or all
            if (!(isValidCategory(category) || category == 'all'))
                return false;

            if (!isValidReadStatus(readStatus))
                return false;

            if (category != 'all' && readStatus != 'all') {
                return notifications[category][readStatus];
            }
            else if (category == 'all' && readStatus == 'all') {
                var notifs = {}, tmp = {};

                for (var i = 0; i < categories.length; i++) {
                    $.extend(tmp, notifications[category[i]].read, notifications[category[i]].unread);
                    $.extend(notifs, tmp);
                }

                return notifs;
            }
            else if (category == 'all' && readStatus != 'all') {
                var notifs = {};

                for (var i = 0; i < categories.length; i++) {
                    $.extend(notifs, notifications[categories[i]][readStatus]);
                }
                return notifs;
            }
            else if (category != 'all' && readStatus == 'all') {
                return $.extend({}, notifications[category].read, notifications[category].unread)
            }
            else return false;
        }

        function runCallback(callback) {
            var functionArgs = Array.prototype.slice.call(arguments, 1);

            if ($.isFunction(callback)) {
                callback.apply(this, functionArgs);
            }
        }


        //Menu Items
        MenuItem = function(category, selector) {
            this.category = category;
            this.$item = $(selector);
            this.$bubble = {};
            this.notificationList = false;
            this.colors = options.bubble.colors;
            this.init();
        };


        MenuItem.prototype.init = function() {
            var self = this;

            this.createBubble();

            if (options.showNotificationList) {
                this.$bubble.bind('click', function() {
                    if (!self.notificationList) {
                        self.notificationList = new NotificationList({
                            category:self.category,
                            $anchor: (options.notificationList.anchor == 'bubble') ? self.$bubble : self.$item
                        });
                    }
                });
            }

            menuItemPrevCount[this.category] = 0;
        };


        MenuItem.prototype.createBubble = function() {
            this.$bubble = $(markup.notificationBubble).html('0').appendTo(this.$item);

            //give the bubble a background color
            if (options.bubble.useColors) {
                if (colorIndex >= this.colors.length)
                    colorIndex = 0;

                this.$bubble.css('background-color', this.colors[colorIndex]);
                colorIndex++;
            }

            if (options.bubble.showEmptyBubble)
                this.$bubble.css('display', 'inline');
        };


        MenuItem.prototype.updateBubble = function() {
            var count = (notifications[this.category]) ? notifications[this.category].unreadCount : 0;

            this.$bubble.html(count);

            //there are no unread notidications. Hide teh bubble
            if (count <= 0 && !options.bubble.showEmptyBubble)
                this.$bubble.stop().fadeOut('fast');

            //if there are undread notifications, and the unread count was previously 0, then the bubble will be hidden
            //we need to show it
            if (count > 0 && (menuItemPrevCount[this.category] == 0))
                this.$bubble.stop().fadeIn();

            //The notification count has increased
            if (menuItemPrevCount[this.category] < count)
                runCallback(options.notificationCountIncrease, this);

            menuItemPrevCount[this.category] = count;
        };


        //Notification List
        NotificationList = function(notificationListOptions) {
            this.$wrapper = {};
            this.$list = false;
            this.type = 'unread';

            this.markup = {
                notificationListWrapper:'<div class="notification-list-wrapper"></div>',
                notificationListMenu:'<ul class="notification-list-menu">' +
                        '<li id="unread-menu-item" class="notification-list-menu-item">'+options.language.unread+'</li>' +
                        '<li id="all-menu-item" class="notification-list-menu-item">'+options.language.all+'</li>' +
                        '<li class="close-notification-list"></li>' +
                        '</ul>',
                notificationListMenuItem:'<li class="notification-list-menu-item"></li>',
                notificationList:'<ul class="notification-list"></ul>',
                notificationListItem:'<li class="notification-list-item"></li>'
            };

            this.settings = $.extend(true, {}, options.notificationList, notificationListOptions);

            if (notificationList) {
                var self = this;
                notificationList.close(function() {
                    self.build();
                });
            }
            else this.build();

            return this;
        };


        NotificationList.prototype.build = function() {
            var $menu;

            this.$wrapper = $(this.markup.notificationListWrapper);

            if (this.settings.showMenu) {
                $menu = $(this.markup.notificationListMenu);
                this.$wrapper.append($menu);
            }

            this.populate();

            this.bindMenuActions();

            this.$wrapper.appendTo(options.anchor).position({
                my:'center top',
                at:'center bottom',
                of:this.settings.$anchor,
                offset: this.settings.offset
            });

            this.$wrapper.css('display', 'block').css('top','54px').animate({opacity:1});

            notificationList = this;
        };


        NotificationList.prototype.populate = function() {
            var $list, self = this, theNotifications, listClass;


            $list = $(this.markup.notificationList).attr('data-type', self.type);

            theNotifications = getNotifications(this.settings.category, this.type);

            if (!$.isEmptyObject(theNotifications)) {
                $.each(theNotifications, function(i, notification) {
                    listClass = notification.settings.icon ? 'show-icon' : '';

                    $(self.markup.notificationListItem).addClass(listClass).append(notification.getHtml()).data({
                        id: notification.id,
                        category:notification.category,
                        notification:notification
                    }).appendTo($list);
                });
            }
            else {
                $(self.markup.notificationListItem).addClass('empty-list').html(options.notificationListEmptyText).appendTo($list);
            }

            //bind click action
            $list.find(cssSelector.notificationListItem).bind('click', function(e){
            	
            	if (e.target === 'undefined' || e.target.className != 'close') {
            		runCallback(options.notificationClickCallback, $(this).data('notification'));
            	}
            });

            if (this.$list)
                this.$list.remove();

            this.$list = $list;
            this.$wrapper.append(this.$list);
        };


        NotificationList.prototype.bindMenuActions = function() {
            var self = this, thisNotification;

            //re-populate the notification list when a menu item is clicked
            this.$wrapper.find(cssSelector.notificationListMenuItem).bind('click', function() {
                self.type = $(this).attr('id').split('-')[0];
                self.populate();
            });

            //handle clicks of mark read
            this.$wrapper.delegate('.close', 'click', function() {
                var $this = $(this);

                $this.parents(cssSelector.notificationListItem).animate({opacity:0}, 400, function() {
                    var $this = $(this), data = $this.data();

                    //we are verifying the existence of each level to prevent an undefined error, which occurs if the button is clicked more than once
                    if (notifications && notifications[data.category] && notifications[data.category][self.type]) {
                        if (notifications[data.category][self.type][data.id]) {
                            thisNotification = notifications[data.category][self.type][data.id].markRead();
                            menuItems[data.category].updateBubble();
                        }
                    }

                    $this.remove();
                    runCallback(options.markReadCallback, thisNotification);
                });
            });

            //handle close button click
            this.$wrapper.find(cssSelector.notificationListCloseButton).bind('click', function() {
                self.close();
            });
        };


        NotificationList.prototype.close = function(callback) {
            var self = this;

            this.$wrapper.fadeOut(50, function() {
                self.$wrapper.remove();
                menuItems[self.settings.category].notificationList = false;
                notificationList = false;
                runCallback(callback);
            });
        };





        //Notifications
        Notification = function(notificationOptions) {
            this.id = '';
            this.message = {};
            this.category = '';



            this.$notification = false; //default to false rather than an empty object so we can easily test if it's been set w/ !this.$notification


            this.read = false;

            //Use the default options if the notification is created with just the message (no other options)
            if (typeof notificationOptions == 'string') {
                this.settings = options.notification;
                this.settings.message = notificationOptions;
            }
            else this.settings = notificationOptions;
        };


        Notification.prototype.create = function() {

            if (this.settings.message) {

                //give the notification a unique id
                this.id = this.settings.id || uniqueId('notification');
                this.message = this.settings.message;
                this.category = this.settings.category ? this.settings.category : options.defaultCategory;
                //if there is a value for read(perhaps from the db), use it. Otherwise use default
                this.read = (typeof this.settings.read != 'undefined') ? this.settings.read : this.read;
                
                return this;
            }
            else return false;
        };


        Notification.prototype.html = function() {
            this.$notification = $(markup.notification);

            //remove the icon if this notification does not have one
            if (!this.settings.icon)
                this.$notification.find(cssSelector.notificationIcon).remove();

            //set the notifications id to the id we just created
            this.$notification.attr('id', this.id);

            if (this.settings.notificationClass)
                this.$notification.addClass(this.settings.notificationClass);

            this.setValues();

        };


        Notification.prototype.getHtml = function() {
            //THe notification can be displayed in multiple locations which is the reason for this function. Each location
            //will need its own copy of the html. For example, a notification can be displayed in a notification list
            //and in a modal at the same time
            if (!this.$notification)
                this.html();

            //return a copy of the html, we would just be moving around the same dom element, which won't work if the
            //notification needs to be displayed in multiple places at the same time
            return this.$notification.clone();
        };


        Notification.prototype.setValues = function() {
            //set the notification icon
            if (typeof this.settings.icon != 'undefined')
                this.$notification.addClass('show-icon').find('.icon')
                        .css('background', 'transparent url(' + this.settings.icon + ') no-repeat center center scroll')
                        .html(markup.notificationIcon).find('img').attr('src', this.settings.icon); //attr('src', this.settings.icon);

            //set the notification message
            if (typeof this.message != 'undefined')
                this.$notification.find('.message').html(this.message);
        };


        Notification.prototype.markRead = function() {
            this.read = true;

            notifications[this.category].readCount++;
            notifications[this.category].read[this.id] = this;

            notifications[this.category].unreadCount--;
            delete notifications[this.category].unread[this.id];

            return this;
        };


        Notification.prototype.destroy = function(){
            unregisterNotification(this);
        };

        //API
        notificationMenu.createNotification = function(notificationOptions) {
            var notification = new Notification(notificationOptions);

            if (notification.create()) {
                registerNotification(notification);
                runCallback(options.createCallback, notification);

            }

            return notification;
        };


        notificationMenu.initMenu = function(menuItems) {
            $.each(menuItems, function(category, selector) {
                var menuItem = new MenuItem(category, selector);
                createCategory(category, menuItem);
            });
        };


        notificationMenu.getNotifications = function(category, type) {
            return getNotifications(category, type);
        };


        notificationMenu.deleteNotification = function(notification){
            notification.destroy();
        };


        notificationMenu.notifications = notifications;

        init();

        return notificationMenu;

    };

})(jQuery);