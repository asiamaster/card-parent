/**
 * @return {number}
 */
function yuanToFen(val) {
    if (val == undefined) {
        return 0;
    }
    return parseFloat(val) * 100;
}

