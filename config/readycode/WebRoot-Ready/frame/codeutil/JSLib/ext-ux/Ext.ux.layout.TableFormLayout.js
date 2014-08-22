//tableform layout 有一个bug：不应该将hiddern纳入布局对象
Ext.namespace("Ext.ux.layout");
Ext.ux.layout.TableFormLayout = Ext.extend(Ext.layout.TableLayout,{
    monitorResize: true,
    trackLabels: Ext.layout.FormLayout.prototype.trackLabels,
    setContainer: function() {
        Ext.layout.FormLayout.prototype.setContainer.apply(this, arguments);
        this.currentRow = 0;
        this.currentColumn = 0;
        this.cells = [];
    },
    renderItem : function(c, position, target) {
        if (c && !c.rendered) {
            var cell = Ext.get(this.getNextCell(c));
            cell.addClass("x-table-layout-column-" + this.currentColumn);
            Ext.layout.FormLayout.prototype.renderItem.call(this, c, 0, cell);
        }
    },
    getTemplateArgs : Ext.layout.FormLayout.prototype.getTemplateArgs,
    isValidParent : Ext.layout.FormLayout.prototype.isValidParent,
    onRemove : Ext.layout.FormLayout.prototype.onRemove,
    isHide : Ext.layout.FormLayout.prototype.isHide,
    onFieldShow : Ext.layout.FormLayout.prototype.onFieldShow,
    onFieldHide : Ext.layout.FormLayout.prototype.onFieldHide,
    getLabelStyle : Ext.layout.FormLayout.prototype.getLabelStyle,
    onLayout : function(ct, target) {
        Ext.ux.layout.TableFormLayout.superclass.onLayout.call(this, ct, target);
        if (!target.hasClass("x-table-form-layout-ct")) {
            target.addClass("x-table-form-layout-ct");
        }
    }
});
Ext.Container.LAYOUTS["tableform"] = Ext.ux.layout.TableFormLayout;