package com.berka.multiplanner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.berka.multiplanner.Models.Travel.Segment;
import com.berka.multiplanner.Models.Trips.Location;
import com.berka.multiplanner.Planner.Planner;
import com.berka.multiplanner.fragments.DetailsFragment;
import com.berka.multiplanner.fragments.ResultFragment;
import com.berka.multiplanner.fragments.SearchFragment;
import com.groupalpha.berka.multiplanner.R;

public class MainActivity extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		int count = mSectionsPagerAdapter.getCount();

		switch (count) {
		case 0: {
			Planner p = ((SearchFragment) mSectionsPagerAdapter.getItem(0))
					.getPlanner();
			if (p != null) {
				LinkedList<String> list = new LinkedList<String>();
				for (Location l : p.getFrom())
					list.add(l.getTheJSONBluePrint().toString());
				outState.putStringArray("FROM", (String[]) list.toArray());
				list.clear();
				outState.putString("TO", p.getTo().getTheJSONBluePrint()
						.toString());

			}
		}
			break;
		case 1:
			break;
		case 2:
			break;
		}
	}

	public ViewPager getViewPager() {

		return mViewPager;
	}

	public SectionsPagerAdapter getAdapter() {
		return mSectionsPagerAdapter;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// if(savedInstanceState ==null){

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager(), this, mViewPager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setOffscreenPageLimit(4);

		// Set up the ViewPager with the sections adapter.

		Log.d("here", ":)");

	}

	@Override
	public void onBackPressed() {
		if (mViewPager.getCurrentItem() == 0) {
			super.onBackPressed();
		} else {
			mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		MainActivity ac;
		ArrayList<Fragment> fragments = new ArrayList<Fragment>();
		ViewPager pager;
		FragmentManager fm;

		boolean displayDetails = false;

		public SectionsPagerAdapter(FragmentManager fm, Activity ac,
				ViewPager pager) {

			super(fm);
			this.fm = fm;
			SearchFragment f = new SearchFragment();
			f.setRetainInstance(true);
			fragments.add(f);
			this.ac = (MainActivity) ac;
			this.pager = pager;
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			return fragments.get(position);
		}

		public void SHowResults() {
			ResultFragment fra = new ResultFragment();
			// AddItem(fra);
			fragments.add(fra);
		}

		private DetailsFragment instanceiateDetails() {
			if (fragments.size() != 3) {
				DetailsFragment f = new DetailsFragment();
				fragments.add(f);
				return f;
			} else
				return (DetailsFragment) fragments.get(2);
		}

		public void ShowDetails(List<List<Segment>> data) {
			displayDetails = true;
			if (getCount() <= 2) {

				DetailsFragment fra = instanceiateDetails();
				fra.setRetainInstance(true);
				fra.setData(data);

				this.notifyDataSetChanged();
				int count = fragments.size();
				ac.getViewPager().setCurrentItem(count - 1, true);
				this.notifyDataSetChanged();
				// AddItem(fra);
			} else {
				DetailsFragment fra = (DetailsFragment) fragments.get(fragments
						.size() - 1);
				this.notifyDataSetChanged();
				ac.getViewPager().setCurrentItem(fragments.size() - 1, true);
				fra.setData(data);

			}
		}

		private void removeAllbutFirst() {
			while (getCount() > 1) {

				fragments.remove(getCount() - 1);
			}
			// this.notifyDataSetChanged();
		}

		public void ShowResults(Planner planner) {
			ResultFragment fra;

			if (fragments.size() <= 1) {
				fra = new ResultFragment();
				fra.setRetainInstance(true);
				fra.setPlanner(planner);
				fragments.add(fra);
				this.notifyDataSetChanged();
				ac.getViewPager().setCurrentItem(getCount() - 1, true);

			} else if (fragments.size() == 3) {
				DetailsFragment f = (DetailsFragment) fragments.get(2);
				f.invalidateData();
				displayDetails = false;
				fra = (ResultFragment) fragments.get(1);
				// DetailsFragment fragment2 =
				// (DetailsFragment)fragments.get(2);
				fra.setPlanner(planner);
				this.notifyDataSetChanged();

				ac.getViewPager().setCurrentItem(1, true);
				// removeDetailFramgent();
				this.notifyDataSetChanged();

			} else {
				fra = (ResultFragment) fragments.get(1);
				fra.setPlanner(planner);
				this.notifyDataSetChanged();
				ac.getViewPager().setCurrentItem(getCount() - 1, true);
			}

		}

		private void removeDetailFramgent() {
			FragmentTransaction trans = fm.beginTransaction();
			trans = trans.remove(fragments.get(2));
			fragments.remove(2);
			trans.commitAllowingStateLoss();

		}

		private void addDetailFramgent(Fragment fragment) {
			FragmentTransaction trans = fm.beginTransaction();
			// trans = trans.add(fragment,fragment.getTag());
			trans = trans.add(fragment, fragment.getTag());
			fragments.add(fragment);
			trans.commitAllowingStateLoss();

		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			super.destroyItem(container, position, object);
			fragments.remove(position);
		}

		private void AddItemGraceful(Fragment fragment) {
			if (fragment instanceof ResultFragment && getCount() == 1) {
				fragments.add(fragment);
			} else if (fragment instanceof DetailsFragment && getCount() == 2) {
				fragments.add(fragment);
			} else if (fragment instanceof ResultFragment && getCount() == 3) {
				((DetailsFragment) fragments.get(2)).invalidateData();
			} else if (fragment instanceof ResultFragment && getCount() == 2) {
				;
			}
		}

		public void AddItem(Fragment fragment) {
			// AddItemGraceful(fragment);

			this.notifyDataSetChanged();
			ac.getViewPager().setCurrentItem(getCount() - 1, true);
		}

		@Override
		public int getCount() {
			// Show 3 total pages.

			if (fragments.size() > 2) {
				if (displayDetails)
					return fragments.size();
				else
					return fragments.size() - 1;
			} else
				return fragments.size();

			// if(fragments.size()>2 && displayDetails)
			// return fragments.size();
			// else if(fragments.size() == 2 && !displayDetails)
			// return 2;
			// else
			// // return 1;

		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return "SÖK";
				// getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return "RESA FRÅN";
				// getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return "RESPLAN";
				// getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_dummy,
					container, false);
			TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return rootView;
		}
	}

}
