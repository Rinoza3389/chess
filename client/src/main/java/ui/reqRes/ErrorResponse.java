package ui.reqRes;

import java.util.Objects;

public final record ErrorResponse (Integer status, String message){}

