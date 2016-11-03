/**
 * jQuery SelectDescription plugin
 *
 * Copyright (c) 2014 Javier Hernandez
 * URL www.elapunte.com
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
 *
 */

(function($){
	$.fn.selectDescription = function(method) {
		
		// Opciones del plugin
		var settings = {
			descriptionAttribute : 'description', // Name for description attribute in tag option
			width : '80%', // width for select
			callback: function($data){}, // Execute callback
			icon : 'images/info.png', // Icon to show
			positions: 'top' // Position of bt
		};
		
		// Metodos publicos
		var methods = {
		    init : function( options ) { // Constructor
				
				// Actualizamos settings
				if ( options ) { $.extend( settings, options ); }
				
				return this.each(function() {
					
					var $this = $(this);
					
					// Guardamos las opciones del plugin para accesso desde los metodos
					$this.data(settings);
					
					var $parent = $this.parent();
					$parent.append('<nobr/>');
					var $wrapper = $parent.find('nobr');
					$this.appendTo($wrapper);
					$this.css('width', settings.width);
					$wrapper.append($('<img/>',{src:settings.icon, 'class':'hide'}));
					
					// Asignamos el evento
					$this.bind('change', methods.changeDescription);
					
					// Llamamos al evento para inicializar la select
					$this.trigger('change');
				});
			},
		    changeDescription : function(value) {
		    	
				(typeof this.each === 'function'?this:$(this)).each(function() {
					
					var $this = $(this);

					settings = $this.data(); // Recuperamos las opciones
					
					var description = $this.find('option:selected').attr(settings.descriptionAttribute);
					
					if (description != null && description !== 'undefined' && description !== '') {
						
						var $img = $this.parent().find('img');
						
						$img.prop('title', description);
						$img.attr('bt-xtitle', description);
						$img.show();
						
						$img.bt({
							fill: '#F9FBFF',
							cssStyles: {color: '#343C4E', width: 'auto'},
							width: 250,
							padding: 10,
							cornerRadius: 5,
							spikeLength: 15,
							spikeGirth: 5,
							shadow: true,
							positions: settings.positions
						});
					}
					else {
						$this.parent().find('img').hide();
					}
					
					if (typeof settings.callback === 'function') {
						
						settings.callback($this);
					}
				});
			}
		  };
		
		// Parte logica
		if ( methods[method] ) { // Llamamos a un metodo si existe
			return methods[ method ].apply( this, Array.prototype.slice.call( arguments, 1 ));
		} else if ( typeof method === 'object' || ! method ) { // Llamamos al contructor
			return methods.init.apply( this, arguments );
		} else {
			$.error( 'Method ' +  method + ' does not exist on jQuery.multiSelect' );
		}
	};
})( jQuery );