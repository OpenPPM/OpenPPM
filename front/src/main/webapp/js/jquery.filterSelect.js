/**
 * jQuery FilterSelect plugin
 *
 * Copyright (c) 2011 Javier Hernandez
 * URL www.elapunte.com
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
 *
 */
(function($){
	$.fn.filterSelect = function(method) {
		
		// Opciones del plugin
		var settings = {
			'selectFilter' : '', // Name for idSelector for target
			'methodAjax' : false, // Use load by AJAX
			'valueParam' : 'value', // Name of value param - methodAjax = true
			'textParam' : 'text', // Name of text param - methodAjax = true
			'urlAjax' : '', // URL for load - methodAjax = true
			'emptyAll': false, // Si no hay nada seleccionado mostramos todos los valores
			'prefix': '', // Prefix for class
			'showEmpty': false, // Mostramos los elementos que tienen el valor de 'classEmpty' por defecto es empty
			'classEmpty': 'empty', // Clase para mostrar si configuramos 'showEmpty' = true
			'between' : false, // Miramos rangos con el atributo since = class y para el until creamos un atributo until en el option
			'disabledEmpty' : false // Si no hay nada seleccionado en el origen el destino lo desabilitamos
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
					
					if (settings.selectFilter == '' || settings.selectFilter == null) { $.error('The setting selectFilter is required for jQuery.multiSelect'); }
					if (!settings.methodAjax) { // Si no es por Ajax preparamos la funcionalidad
						
						// Creamos un select oculto con todos los valores
						var $childs = $('#'+settings.selectFilter+ ' option').clone();
						var $newSelect = $('<select id="'+settings.selectFilter+'SelectFilter" style="display:none;"></select');
						$newSelect.append($childs );
						$('body').append($newSelect);
					}
					
					// Asignamos el evento
					$this.bind('change', methods.filter);
					
					// Llamamos al evento para inicializar la select
					$this.trigger('change');
				});
			},
		    filter : function(value) {
		    	
				(typeof this.each === 'function'?this:$(this)).each(function() {
					
					var $this = $(this);

					settings = $this.data(); // Recuperamos las opciones
					
					var $child = $('#'+settings.selectFilter);
					
					if (settings.methodAjax) { // Usamos la funcionalidad por AJAX
						
						$.ajax({
							dataType: 'json', type: "POST", url: settings.urlAjax, data: ({type : $this.val()}), 
							success: function(data) {
								var options = '';
								$(data).each(function() { // Cargamos la informacion por AJAX
									options += '<option value="'+this[settings.valueParam]+'">'+this[settings.textParam]+'</option>';
								});
								$child.html(options);
							}
						});
					}
					else {
						// Refrescamos la select con los nuevos valores
						var values = $this.val();
						
						if (settings.disabledEmpty && values == '') {
							$child.prop('disabled','true');
						}
						else if ($child.prop('disabled')) {
							$child.prop('disabled','');
						}
						
						if (values == null || values == 'null' || (settings.emptyAll && values == '')) {
							var $childTemp	= $('#'+settings.selectFilter+'SelectFilter option');
							$child.html($childTemp.clone());
						}
						else {
							$child.html('');
							
							if (settings.between) {
								var filterValue = parseInt(values);
								
								$('#'+settings.selectFilter+'SelectFilter option').each(function() {
									
									var $option 	= $(this); 
									var since		= parseInt($option.attr('class'));
									var until		= parseInt($option.attr('until'));
									
									if (!isNaN(since) && !isNaN(filterValue) &&
											((!isNaN(until) && filterValue >= since && filterValue <= until)
											|| since == filterValue) || (isNaN(since) && isNaN(filterValue))) {
										$child.append($option.clone());
									}
									
								});
								if (settings.showEmpty) {
									var $childTemp = $('#'+settings.selectFilter+'SelectFilter .'+settings.prefix+settings.classEmpty);
									$child.append($childTemp.clone());
								}
							}
							else {
								if (typeof values === 'object') {
								
									$(values).each(function() {
										var $childTemp = $('#'+settings.selectFilter+'SelectFilter .'+settings.prefix+this);
										$child.append($childTemp.clone());
									});
									if (settings.showEmpty) {
										var $childTemp = $('#'+settings.selectFilter+'SelectFilter .'+settings.prefix+settings.classEmpty);
										$child.append($childTemp.clone());
									}
								}
								else {
									
									var selector = '#'+settings.selectFilter+'SelectFilter .'+settings.prefix+values;
									if (settings.showEmpty) {
										selector += ',#'+settings.selectFilter+'SelectFilter .'+settings.prefix+settings.classEmpty;
									}
									
									var $childTemp = $(selector);
									$child.html($childTemp.clone());
								}
							}
						}
						$child.val('');
					}
					if (typeof value === 'string') { $child.val(value); } // Si le pasamos un valor por defecto lo aplicamos
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