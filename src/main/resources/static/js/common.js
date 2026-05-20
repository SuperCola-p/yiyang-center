/**
 * 东软颐养中心管理系统 - 通用JS工具
 */

// Toast 提示
function showToast(message, type) {
    type = type || 'success';
    var bgColor = type === 'success' ? '#28a745' :
                  type === 'error' ? '#dc3545' :
                  type === 'warning' ? '#ffc107' : '#17a2b8';
    var toast = $('<div class="toast-notification">' + message + '</div>')
        .css({
            position: 'fixed',
            top: '70px',
            right: '20px',
            background: bgColor,
            color: '#fff',
            padding: '12px 24px',
            borderRadius: '6px',
            zIndex: 9999,
            boxShadow: '0 4px 12px rgba(0,0,0,0.15)',
            fontSize: '14px',
            fontWeight: '500'
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
