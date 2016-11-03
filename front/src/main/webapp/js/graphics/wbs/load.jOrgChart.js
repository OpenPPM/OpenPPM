function initWbs(data, id) {

	var $ul = $('<ul/>', {id : typeof id == 'undefined' ? 'wbsOrg': id, style: 'display:none;'});
	var $li = createWBSLi(data,{ 'class': 'wbsParent' });
	$li.append(createWBS(data.childs));
	$ul.append($li);

	return $ul;
}

function createWBS(nodes) {
	var $ul = $('<ul/>');
	$(nodes).each(function() {

		var $li = createWBSLi(this);
		if (typeof this.childs !== 'undefined' && this.childs.length > 0) {
			$li.append(createWBS(this.childs));
		}
		$ul.append($li);
	});
	return $ul;
}

function createWBSLi(node, type) {
	if (typeof type === 'undefined') {
		
		var stringClass = "";
		
		if (node.type == 'CA') {
			if (node.noBudget == true) {
				stringClass = 'noBudget';
			}
			else {
				stringClass = 'wbsIsCa';
			}
		}
		else if (node.type == 'WG') {
			stringClass = 'wbsNotCa';
		}
		else if (node.type == 'teammember') {
			stringClass = 'obsTeammember';
		}
		
		if (typeof node.seller !== 'undefined') {
			stringClass += ' ' + 'wbsAssociatedSeller';
		}
		
		var type = { 'class': stringClass};
	}
	
	var $li = $('<li/>', type);
	
	$li.append($('<p/>', {text: node.name}));
	
	if(typeof node.pocAbsolute !== 'undefined') {
		$li.append($('<span/>', {text:node.pocAbsolute})); 
	}
	if (typeof node.pocRelative !== 'undefined') { 
		$li.append($('<span/>', {text:'('+ node.pocRelative + ')'}));
	}
	
	/* Seller */
	if (typeof node.seller !== 'undefined') { 
		$li.append($('<img/>', {"class":"seller btitleChart", src: "images/info.png", "bt-xtitle": node.seller, style:"padding-left: 3px; position:relative; top:3px;"}));
		$li.append($('<span/>', {"class":"seller", text: node.seller, style:"display:none; color:#444444; font-weight:normal;"}));
	}
	
	return $li;
}