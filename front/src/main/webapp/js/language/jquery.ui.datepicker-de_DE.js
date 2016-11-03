jQuery(function($){
	$.datepicker.regional['de'] = {
		closeText: 'Schließen',
		prevText: '&#x3C;Zurück',
		nextText: 'Vor&#x3E;',
		currentText: 'Heute',
		monthNames: ['Januar','Februar','März','April','Mai','Juni','Juli','August','September','Oktober','November','Dezember'],
		monthNamesShort: ['Jan', 'Feb', 'Mär', 'Apr', 'Mai', 'Jun',
		'Jul', 'Aug', 'Sep', 'Okt', 'Nov', 'Dez'],
		dayNames: ['Sonntag', 'Montag', 'Dienstag', 'Mittwoch', 'Donnerstag', 'Freitag', 'Samstag'],
		dayNamesShort: ['So', 'Mo', 'Di', 'Mi', 'Do', 'Fr', 'Sa'],
		dayNamesMin: ['So','Mo','Di','Mi','Do','Fr','Sa'],
		weekHeader: 'KW',
		dateFormat: 'dd/mm/yy',
		firstDay: 1,
		showOn: 'button',
		buttonImage: 'images/calendario.png',
		buttonImageOnly: true,
		changeMonth: true,
		changeYear: true};
	$.datepicker.setDefaults($.datepicker.regional['de']);
});
