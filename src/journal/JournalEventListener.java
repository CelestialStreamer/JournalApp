package journal;

/**
 * Interface for an event listener
 * 
 * @author Brian Woodruff
 *
 */
public interface JournalEventListener extends java.util.EventListener {
  public void updateLoadStatus(JournalEvent event);
}
