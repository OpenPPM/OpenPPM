<script type="text/javascript">
    <!--
    var pluginAjax 	= new AjaxCall('plugin','${resourceBundle.getString('error')}');

    var synchronize = {
        load: function() {
            jQuery(".button-synchronize").button();

            jQuery("#searchFilePopup").dialog({
                autoOpen: false, width: 400,
                resizable: false, modal: true,
                height: 120,
                open: function() {
                    jQuery('#file').val('');
                    jQuery('#filePath').val('');
                    jQuery("#btnAcceptImport" ).button( "option", "disabled", true );
                }
            });

            jQuery('#fileImport').change(synchronize.change);
        },
        open : function() {
            jQuery("#searchFilePopup").dialog('open');
        },
        close : function() {
            jQuery("#searchFilePopup").dialog('close');
        },
        confirm : function() {

            var params = {
                operationPlugin: 'SynchronizeProject',
                pluginAction: 'HasMembersAssignedAction',
                idProject: '${idProject}'
            };

            pluginAjax.call(params, function(data) {
                if (data.assigned) {
                    confirmUI('${resourceBundle.getString('msg.confirm')}',
                            '${resourceBundle.getString('msg.warning.members_assigned')}',
                            '${resourceBundle.getString('yes')}','${resourceBundle.getString('no')}',
                            synchronize.open);
                }
                else { synchronize.open(); }
            });
        },
        import: function() {
            if (!jQuery("#btnAcceptImport" ).button( "option", "disabled")) {
                jQuery('#searchFilePopup').dialog('close');

                // Ajax call to upload the file and print the response
                jQuery.ajaxFileUpload ({
                    url: 'plugin',
                    secureuri: false,
                    fileElementId: 'fileImport',
                    dataType: 'html',
                    data: [['operationPlugin', "SynchronizeProject"],
                           ['pluginAction', "ImportSynchronizeAction"],
                           ['id',jQuery('#id').val()],
                           ['idprojectcharter', jQuery('#idprojectcharter').val()]
                        ],
                    beforeSend: function () {jQuery("#loading").show('fast');},
                    success: function (data) {

                        if (data.error) {
                            alertUI('${resourceBundle.getString('error')}',data.error);
                        }
                        else {
                          jQuery('#container').html(data);
                        }
                    },
                    error: function (data, status, e) {
                        console.log("error", data, status, e);
                    }
                });

            }
        },
        export: function () {
            document.frm_synchronize.submit();
        },
        change: function() {
            jQuery('#filePath').val(jQuery(this).val());
            jQuery("#btnAcceptImport" ).button( "option", "disabled", false );
        }
    };
    jQuery(document).ready(synchronize.load);
    //-->
</script>

<a href="javascript:synchronize.export();" class="button-synchronize ui-special">${resourceBundle.getString("export_project")}</a>
&nbsp;
<a href="javascript:synchronize.confirm();" class="button-synchronize ui-special">${resourceBundle.getString("import_project")}</a>

<form name="frm_synchronize" method="POST" action="plugin">
    <input type="hidden" name="operationPlugin" value="SynchronizeProject" />
    <input type="hidden" name="pluginAction" value="ExportSynchronizeAction" />
    <input type="hidden" id="id" name="id" value="${idProject}" />
</form>
<div id="searchFilePopup" title="${resourceBundle.getString('file.export_from_openppm')}" class="popup">
    <form name="frm_synchronize_file">
        <fieldset style="padding: 10px;">
            <input type="text" name="filePath" id="filePath" class="campo" style="width: 300px;" readonly />
            <label class="file_change" style="margin-right:5px; *margin-top: -15px !important;">
                <input type="file" id="fileImport" name="file" class="file" style="width: 0;"/>
            </label>
        </fieldset>
    </form>
    <div class="popButtons">
        <a id="btnAcceptImport" href="javascript:synchronize.import();" class="button-synchronize">${resourceBundle.getString('accept')}</a>
        <a href="javascript:synchronize.close();" class="button-synchronize">${resourceBundle.getString('cancel')}</a>
    </div>
</div>