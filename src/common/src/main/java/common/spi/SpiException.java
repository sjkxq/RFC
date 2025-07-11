package common.spi;

/**
 * SPI加载异常
 */
public class SpiException extends RuntimeException {
    public SpiException(String message) {
        super(message);
    }

    public SpiException(String message, Throwable cause) {
        super(message, cause);
    }
}