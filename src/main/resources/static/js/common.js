/**
 * 东软颐养中心管理系统 - 通用JS工具
 */

// Toast 提示
function showToast(message, type) {
    type = type || 'success';
    var bgColor = type === 'success' ? 'linear-gradient(135deg, #11998e 0%, #38ef7d 100%)' :
                  type === 'error' ? 'linear-gradient(135deg, #ff6b6b 0%, #ee5a24 100%)' :
                  type === 'warning' ? 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)' : 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)';
    var icons = {
        'success': 'fa-check-circle',
        'error': 'fa-exclamation-circle',
        'warning': 'fa-exclamation-triangle',
        'info': 'fa-info-circle'
    };
    var icon = icons[type] || 'fa-check-circle';
    var toast = $('<div class="toast-notification d-flex align-items-center">' +
        '<i class="fas ' + icon + ' mr-2"></i>' +
        '<span>' + message + '</span>' +
        '</div>')
        .css({
            position: 'fixed',
            top: '20px',
            right: '20px',
            background: bgColor,
            color: '#fff',
            padding: '14px 24px',
            borderRadius: '10px',
            zIndex: 9999,
            boxShadow: '0 6px 20px rgba(0,0,0,0.15)',
            fontSize: '14px',
            fontWeight: '500',
            minWidth: '200px',
            maxWidth: '400px'
        });
    $('body').append(toast);
    setTimeout(function() { toast.fadeOut(300, function() { $(this).remove(); }); }, 2500);
}

// 格式化日期时间
function formatDateTime(dateStr) {
    if (!dateStr) return '-';
    var d = new Date(dateStr);
    var pad = function(n) { return n < 10 ? '0' + n : n; };
    return d.getFullYear() + '-' + pad(d.getMonth() + 1) + '-' + pad(d.getDate()) +
        ' ' + pad(d.getHours()) + ':' + pad(d.getMinutes());
}

// 格式化日期
function formatDate(dateStr) {
    if (!dateStr) return '-';
    var d = new Date(dateStr);
    var pad = function(n) { return n < 10 ? '0' + n : n; };
    return d.getFullYear() + '-' + pad(d.getMonth() + 1) + '-' + pad(d.getDate());
}

// 获取API基础路径
function apiBase(module) {
    return '/api/' + module;
}

// 获取URL查询参数
function getQueryParam(name) {
    var url = new URL(window.location.href);
    return url.searchParams.get(name);
}

// HTML转义
function escapeHtml(str) {
    if (!str) return '';
    return str.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
}

// 加载状态占位
function showLoading(selector, colSpan) {
    $(selector).html('<tr><td colspan="' + (colSpan || 8) + '" class="loading-state"><i class="fas fa-spinner fa-spin"></i> 加载中...</td></tr>');
}

// 空状态占位
function showEmpty(selector, colSpan, msg) {
    $(selector).html('<tr><td colspan="' + (colSpan || 8) + '" class="empty-state"><i class="fas fa-inbox"></i><p>' + (msg || '暂无数据') + '</p></td></tr>');
}

// 通用确认弹窗（返回 Promise，resolve(true) 表示确认）
function showConfirm(message, callback) {
    var modalId = 'globalConfirmModal';
    if ($('#' + modalId).length === 0) {
        var html = '<div class="modal fade" id="' + modalId + '" tabindex="-1" role="dialog">' +
            '<div class="modal-dialog modal-sm" role="document">' +
            '<div class="modal-content">' +
            '<div class="modal-header">' +
            '<h5 class="modal-title"><i class="fas fa-exclamation-triangle text-warning"></i> 确认操作</h5>' +
            '<button type="button" class="close" data-dismiss="modal">&times;</button>' +
            '</div>' +
            '<div class="modal-body"><p id="' + modalId + 'Msg"></p></div>' +
            '<div class="modal-footer">' +
            '<button type="button" class="btn btn-secondary btn-sm" data-dismiss="modal">取消</button>' +
            '<button type="button" class="btn btn-primary btn-sm" id="' + modalId + 'Ok">确定</button>' +
            '</div></div></div></div>';
        $('body').append(html);
    }
    $('#' + modalId + 'Msg').text(message);
    $('#' + modalId + 'Ok').off('click').on('click', function() {
        $('#' + modalId).modal('hide');
        if (callback) callback(true);
    });
    $('#' + modalId).on('hidden.bs.modal', function() {
        if (callback) callback(false);
    }).modal('show');
}

