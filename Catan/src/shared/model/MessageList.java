package shared.model;

/**
 * A list of lines that make up a message
 *
 * <pre>
 * <b>Domain:</b>
 * -lines:[MessageLine]
 * </pre>
 * 
 * @author Seth White
 *
 */
public class MessageList {
	private MessageLine[] lines;

	/**
	 * Default constructor, requires all fields
	 * 
	 * @Pre no field may be null
	 * @Post result: a MessageList
	 * @param lines
	 */
	public MessageList(MessageLine[] lines) {
		super();
		this.lines = lines;
	}

	public MessageLine[] getLines() {
		return lines;
	}

	public void setLines(MessageLine[] lines) {
		this.lines = lines;
	}

	public void addLine(MessageLine line) {
		MessageLine[] newLines = new MessageLine[lines.length + 1];
		for (int i = 0; i < lines.length; i++) {
			newLines[i] = lines[i];
		}
		newLines[lines.length] = line;
		lines = newLines;
	}
}
