package com.wanderphone.minesweep;

//import com.minesweep.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class Block extends ImageButton {
	private boolean isCovered; // is block covered yet
	private boolean isMined; // does the block has a mine underneath
	private boolean isFlagged; // is block flagged as a potential mine
	private boolean isQuestionMarked; // is block question marked
	private boolean isClickable; // can block accept click events
	private int numberOfMinesInSurrounding; // number of mines in nearby blocks

	public Block(Context context) {
		super(context);
	}

	public Block(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public Block(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	// set default properties for the block
	public void setDefaults() {
		isCovered = true;
		isMined = false;
		isFlagged = false;
		isQuestionMarked = false;
		isClickable = true;
		numberOfMinesInSurrounding = 0;

		this.setBackgroundResource(R.drawable.mine_coverd);
		setBoldFont();
	}

	// mark the block as disabled/opened
	// update the number of nearby mines
	public void setNumberOfSurroundingMines(int number) {
		updateNumber(number);
	}

	// set mine icon for block
	// set block as disabled/opened if false is passed
	public void setMineIcon(boolean enabled) {
		this.setImageResource(R.drawable.mine_bomb);

		if (!enabled) {
		}
	}

	// set mine as flagged
	// set block as disabled/opened if false is passed
	public void setFlagIcon(boolean enabled) {
		// this.setImageResource(R.drawable.mine_flag);

		if (!enabled) {
			this.setBackgroundResource(R.drawable.mine_coverd);
			// this.setBackgroundColor(R.color.black);

			// this.setTextColor(Color.RED);
		} else {
			// this.setTextColor(Color.BLACK);
			this.setBackgroundResource(R.drawable.mine_flagss);

		}
	}

	// set mine as question mark
	// set block as disabled/opened if false is passed
	public void setQuestionMarkIcon(boolean enabled) {
		// this.setImageResource(R.drawable.q);

		if (!enabled) {
			this.setBackgroundResource(R.drawable.mine_coverd);

		} else {
			this.setBackgroundResource(R.drawable.mine_q);
		}
	}

	// set block as disabled/opened if false is passed
	// else enable/close it
	public void setBlockAsDisabled(boolean enabled) {
		if (!enabled) {
			this.setBackgroundResource(R.drawable.blankl);
		} else {
			this.setBackgroundResource(R.drawable.mine_coverd);
		}
	}

	// clear all icons/text
	public void clearAllIcons() {
		this.setImageResource(INVISIBLE);
	}

	// set font as bold
	private void setBoldFont() {
	}

	// uncover this block
	public void OpenBlock() {
		// cannot uncover a mine which is not covered
		if (!isCovered)
			return;

		setBlockAsDisabled(false);
		isCovered = false;
		// check if it has mine
		if (hasMine()) {
			setMineIcon(false);
		}
		// update with the nearby mine count
		else {
			setNumberOfSurroundingMines(numberOfMinesInSurrounding);

		}
	}

	// set text as nearby mine count
	public void updateNumber(int text) {
		if (text != 0) {
			// select different color for each number
			// we have already skipped 0 mine count
			switch (text) {
			case 1:
				this.setBackgroundResource(R.drawable.num1);
				break;
			case 2:
				this.setBackgroundResource(R.drawable.num2);
				break;
			case 3:
				this.setBackgroundResource(R.drawable.num3);
				break;
			case 4:
				this.setBackgroundResource(R.drawable.num4);
				break;
			case 5:
				this.setBackgroundResource(R.drawable.num5);
				break;
			case 6:
				this.setBackgroundResource(R.drawable.num6);
				break;
			case 7:
				this.setBackgroundResource(R.drawable.num7);
				break;
			case 8:
				this.setBackgroundResource(R.drawable.num8);

				break;
			case 9:
				break;
			}
		}
	}

	// set block as a mine underneath
	public void plantMine() {
		isMined = true;
	}

	// mine was opened
	// change the block icon and color
	public void triggerMine() {
		setMineIcon(true);
		// this.setTextColor(Color.RED);
	}

	// is block still covered
	public boolean isCovered() {
		return isCovered;
	}

	// does the block have any mine underneath
	public boolean hasMine() {
		return isMined;
	}

	// set number of nearby mines
	public void setNumberOfMinesInSurrounding(int number) {
		numberOfMinesInSurrounding = number;
	}

	// get number of nearby mines
	public int getNumberOfMinesInSorrounding() {
		return numberOfMinesInSurrounding;
	}

	// is block marked as flagged
	public boolean isFlagged() {
		return isFlagged;
	}

	// mark block as flagged
	public void setFlagged(boolean flagged) {
		isFlagged = flagged;
	}

	// is block marked as a question mark
	public boolean isQuestionMarked() {
		return isQuestionMarked;
	}

	// set question mark for the block
	public void setQuestionMarked(boolean questionMarked) {
		isQuestionMarked = questionMarked;
	}

	// can block receive click event
	public boolean isClickable() {
		return isClickable;
	}

	// disable block for receive click events
	public void setClickable(boolean clickable) {
		isClickable = clickable;
	}
}
