
var notificationUtils = {
		
		options: {},
		
		// Ajax call
		//
		utilAjax : function() {
			return new AjaxCall(this.options.reference,'Error:');
		},
		// Create notifications by JSON
		//
		createAll : function(data) {
			
			$(data).each(function() {
				
				this.category 	= 'notificationCenter';
				this.icon 		= !this.read?'images/notification/bell.png':'';
		        
		        notiCenter.createNotification(this);
			});
		},
		// Mark read notification
		//
		markRead : function(idContactNotification) {
			
			var params = {
				accion: "ajax-mark-read-notification", 
				idContactNotification: idContactNotification
			};
			
			// Call
			this.utilAjax().call(params);
		},
		// Show popup
		//
		showPopup : function(notification) {
			
			// Resets
			$('#notification-popup div#messageError').hide();
			
			// Fill popup 
			$('#notification-popup #subject').html(notification.message);
			$('#notification-popup #body').html(notification.body);
			$('#notification-popup #creationDate').html(notification.creationDate);
			
			if (notification.sendToMail === 'PENDING') {
				$('#notification-popup #sendToMail').html('<img style="position: relative; width: 16px; height: 16px; top: 3px;" src="images/notification/pending.png">');
			}
			else if (notification.sendToMail === 'SEND') {
				$('#notification-popup #sendToMail').html('<img style="position: relative; width: 16px; height: 16px; top: 3px;" src="images/notification/send.png">');
			}
			else if (notification.sendToMail === 'ERROR') {
				
				$('#notification-popup div#messageError span').html(notification.messageError);
				$('#notification-popup div#messageError').show();
				$('#notification-popup #sendToMail').html('<img style="position: relative; width: 16px; height: 16px; top: 3px;" src="images/notification/error.png">');
			}
			
			// Open popup
			$('#notification-popup').dialog('open');
			
			// Mark read
			this.markRead(notification.idContactNotification);
		}
};

var notiCenter;
readyMethods.add(function () {

	notiCenter = new $.ttwNotificationMenu({
		
		// Language
		//
		language: notificationUtils.options,
		notificationListEmptyText: notificationUtils.options.notificationListEmptyText,
		
		// Properties
		//
		notificationList:{
            offset: '-137 24'
        },
		// Show popup notification
        //
		notificationClickCallback: function(notification) {
			notificationUtils.showPopup(notification.settings);
		},
		// Mark as read
		//
		markReadCallback: function(notification) {
			notificationUtils.markRead(notification.settings.idContactNotification);
		}
	});
	
	//Add bubbles to a menu 
	notiCenter.initMenu({ 
		notificationCenter:'#notificationCenter'
	});
	
});
