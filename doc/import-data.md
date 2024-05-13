# The import data model

The extracted data from the plenary documents are delivered to the backend for import in the data model described below. (voting-data repoitory/transparentdemocracy/model.py)

It is split into two parts, which in the end result in two datasets:
- plenaries and their topics: proposals, motions, interpellations.
- votes cast during plenaries: by which politician, on which motion, which vote (yes/no/abstention).
  
# Plenaries Data

Classes related to the plenaries and their "topics": proposals, motions (and later: interpellations).

```python
@dataclass
class Plenary:
id: str
number: int  # sequence number of the plenary in the series of plenaries during a legislature.
date: date
legislature: int
pdf_report_url: str
html_report_url: str
proposal_discussions: List[ProposalDiscussion]
motions: List[Motion]
report_items: List[ReportItem]


@dataclass
class Proposal:
document_reference: Optional[str]  # official reference in the parliament, as mentioned in plenary reports.
title_nl: str
title_fr: str


@dataclass
class ProposalDiscussion:
id: str
plenary_id: str
plenary_agenda_item_number: int  # item number on the agenda in the plenary session during which the proposal was discussed. This is the number surrounded with a black border seen in all plenary reports.
description_nl: str
description_fr: str
proposals: List[Proposal]  # first proposal is the main one under discussion, optional others are linked proposals.


@dataclass
class Motion:
id: str
number: str  # sequence number of the motion in the series of motions held towards the end of a plenary.
proposal_id: str
cancelled: bool


@dataclass
class BodyTextPart:
lang: str
text: str


@dataclass
class ReportItem:
label: str
nl_title: str
nl_title_tags: List[PageElement]
fr_title: str
fr_title_tags: List[PageElement]
body_text_parts: List[BodyTextPart]
body: List[PageElement]


```

## Classes related to the detail of votes cast in plenaries:

```python
@dataclass
class Vote:
motion_id: str
vote_type: VoteType
politician_id: str

@dataclass
class Politician:
id: str
full_name: str
party: str


class VoteType(Enum):
YES = "YES"
NO = "NO"
ABSTENTION = "ABSTENTION"

```
