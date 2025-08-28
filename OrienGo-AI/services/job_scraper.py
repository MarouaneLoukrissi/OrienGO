import pandas as pd
from jobspy import scrape_jobs

def search_jobs(search_term):
    jobs = scrape_jobs(
        site_name=["linkedin","indeed","google"],
        search_term=search_term,
        google_search_term=search_term + " jobs in Morocco since yesterday",
        #location="Morocco",
        results_wanted=3,
        hours_old=72,
        country_indeed='Morocco'
    )
    
    df = pd.DataFrame(jobs)

    # Return empty list if no results
    if df.empty:
        return []

    # Ensure required columns exist before building salaryRange
    for col, default in [("min_amount", 0), ("max_amount", 0), ("currency", ""), ("interval", "")]:
        if col not in df.columns:
            df[col] = default

    # Drop optional columns if present (avoid errors if missing)
    df = df.drop(columns=["job_level","job_function","emails"], errors="ignore")

    # Build salaryRange safely
    df["salaryRange"] = (
        df["min_amount"].fillna(0).astype(int).astype(str)
        + " - "
        + df["max_amount"].fillna(0).astype(int).astype(str)
        + " "
        + df["currency"].fillna("")
        + "/"
        + df["interval"].fillna("")
    )

    # Remove raw salary columns after composing salaryRange
    df = df.drop(columns=["min_amount","max_amount","currency"], errors="ignore")

    # Normalize column names to backend expectations
    df = df.rename(columns={
        "site":"source",
        "skills":"requiredSkills",
        "job_url":"applyUrl",
        "company":"companyName",
        "job_type":"jobType",
        "date_posted":"postedDate",
        "company_url":"companyUrl",
        "company_url_direct":"companyUrlDirect",
        "company_addresses":"companyAddresses",
        "company_num_employees":"companyNumEmployees",
        "company_revenue":"companyRevenue",
        "company_description":"companyDescription",
        "experience_range":"experienceRange",
        "company_industry":"companyIndustry",
        "job_url_direct":"jobUrlDirect",
        "is_remote":"isRemote"
    })
    
    columns_to_keep = [
        "source","requiredSkills","applyUrl","title","companyName","location","jobType","description",
        "salaryRange","postedDate","companyUrl","companyUrlDirect","companyAddresses","companyNumEmployees",
        "companyRevenue","companyDescription","experienceRange","companyIndustry","jobUrlDirect","isRemote"
    ]
    existing = [c for c in columns_to_keep if c in df.columns]
    df = df[existing]
    df = df.where(pd.notnull(df), None)
    
    return df.to_dict(orient="records")