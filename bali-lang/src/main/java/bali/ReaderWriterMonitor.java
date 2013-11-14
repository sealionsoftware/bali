package bali;

import bali.annotation.MetaType;
import bali.annotation.MetaTypes;
import bali.annotation.Monitor;

/**
 * User: Richard
 * Date: 07/07/13
 */
@Monitor
@MetaType(MetaTypes.INTERFACE)
public interface ReaderWriterMonitor extends Reader, Writer {
}
