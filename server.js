const http = require('http');
const fs = require('fs');
const path = require('path');

const PORT = 3000;
const HOST = '0.0.0.0';

const APK_PATH = path.join(__dirname, '.build-outputs', 'app-debug.apk');

const htmlContent = `<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>NeuroVault: Chemistry Universe</title>
  <meta name="description" content="Chemistry memory adventure and B.Pharm foundation training game with interactive 2D atomic puzzles and spaced review.">
  <meta property="og:title" content="NeuroVault: Chemistry Universe">
  <meta property="og:description" content="Chemistry memory adventure and B.Pharm foundation training game with interactive 2D atomic puzzles and spaced review.">
  <script src="https://cdn.tailwindcss.com"></script>
  <style>
    body { background-color: #070B19; color: #F8FAFC; font-family: system-ui, -apple-system, sans-serif; }
    .neon-cyan { color: #00E5FF; text-shadow: 0 0 10px rgba(0, 229, 255, 0.5); }
    .neon-amber { color: #F59E0B; text-shadow: 0 0 10px rgba(245, 158, 11, 0.5); }
    .neon-emerald { color: #10B981; text-shadow: 0 0 10px rgba(16, 185, 129, 0.5); }
    .neon-violet { color: #C084FC; text-shadow: 0 0 10px rgba(192, 132, 252, 0.5); }
    .scifi-card { background: rgba(30, 41, 59, 0.85); border: 1px solid #334155; border-radius: 1rem; }
    .scifi-btn { background: #00E5FF; color: #070B19; font-weight: 700; border-radius: 0.75rem; transition: all 0.2s; }
    .scifi-btn:hover { background: #38BDF8; box-shadow: 0 0 15px rgba(0, 229, 255, 0.6); }
  </style>
</head>
<body class="min-h-screen p-4 md:p-8 flex flex-col items-center">
  <div class="w-full max-w-4xl space-y-6">
    <!-- Top Bar -->
    <header class="scifi-card p-4 flex flex-wrap justify-between items-center border-[#00E5FF]/40">
      <div class="flex items-center space-x-3">
        <div class="w-10 h-10 rounded-full bg-cyan-950/80 border border-cyan-400 flex items-center justify-center text-cyan-300 font-black text-xl">⚛</div>
        <div>
          <h1 class="text-xl md:text-2xl font-black tracking-wider neon-cyan">NEUROVAULT</h1>
          <p class="text-xs text-slate-400">Chemistry Universe & B.Pharm Foundation</p>
        </div>
      </div>
      <div class="flex items-center space-x-3 mt-2 sm:mt-0">
        <div class="bg-slate-900 border border-amber-400/50 px-3 py-1.5 rounded-full flex items-center space-x-1.5 text-sm font-bold text-amber-300">
          <span>★</span><span id="score-display">0</span> Pts
        </div>
        <a href="/download-apk" class="bg-emerald-600 hover:bg-emerald-500 text-slate-950 font-bold text-xs px-3 py-2 rounded-lg flex items-center space-x-1.5 shadow-lg shadow-emerald-500/20">
          <span>⬇</span><span>Download Android APK</span>
        </a>
      </div>
    </header>

    <!-- Nav Tabs -->
    <nav class="flex overflow-x-auto space-x-2 pb-2">
      <button onclick="switchTab('atom')" class="tab-btn px-4 py-2 rounded-lg font-bold text-sm bg-cyan-500 text-slate-950" id="tab-atom">Atom Assembler</button>
      <button onclick="switchTab('formula')" class="tab-btn px-4 py-2 rounded-lg font-bold text-sm bg-slate-800 text-slate-300" id="tab-formula">Formula Forge</button>
      <button onclick="switchTab('daily')" class="tab-btn px-4 py-2 rounded-lg font-bold text-sm bg-slate-800 text-slate-300" id="tab-daily">Daily Challenge</button>
      <button onclick="switchTab('review')" class="tab-btn px-4 py-2 rounded-lg font-bold text-sm bg-slate-800 text-slate-300" id="tab-review">Spaced Review</button>
      <button onclick="switchTab('syllabus')" class="tab-btn px-4 py-2 rounded-lg font-bold text-sm bg-slate-800 text-slate-300" id="tab-syllabus">B.Pharm Syllabus</button>
    </nav>

    <!-- TAB 1: Atom Assembler -->
    <section id="sec-atom" class="scifi-card p-6 border-cyan-500/30 space-y-6">
      <div class="flex justify-between items-center">
        <div>
          <h2 class="text-lg font-bold text-cyan-300">World 1 — Atom Observatory: Subatomic Particle Assembler</h2>
          <p class="text-xs text-slate-400">Configure protons, neutrons, and electrons to verify Atomic Number (Z), Mass Number (A), and Ionic Charge.</p>
        </div>
        <span class="text-xs font-mono px-2.5 py-1 bg-cyan-950 text-cyan-400 border border-cyan-500/40 rounded">BP106T</span>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
        <!-- Interactive Controls -->
        <div class="space-y-4">
          <div class="bg-slate-900/80 p-3 rounded-lg border border-slate-700">
            <div class="flex justify-between text-xs font-bold text-cyan-400 mb-1">
              <span>Protons (p⁺)</span><span id="p-val" class="font-mono text-base">11 (Na)</span>
            </div>
            <input type="range" min="1" max="18" value="11" id="p-slider" oninput="updateAtom()" class="w-full accent-cyan-400">
          </div>
          <div class="bg-slate-900/80 p-3 rounded-lg border border-slate-700">
            <div class="flex justify-between text-xs font-bold text-purple-400 mb-1">
              <span>Neutrons (n⁰)</span><span id="n-val" class="font-mono text-base">12</span>
            </div>
            <input type="range" min="0" max="22" value="12" id="n-slider" oninput="updateAtom()" class="w-full accent-purple-400">
          </div>
          <div class="bg-slate-900/80 p-3 rounded-lg border border-slate-700">
            <div class="flex justify-between text-xs font-bold text-amber-400 mb-1">
              <span>Electrons (e⁻)</span><span id="e-val" class="font-mono text-base">10</span>
            </div>
            <input type="range" min="0" max="18" value="10" id="e-slider" oninput="updateAtom()" class="w-full accent-amber-400">
          </div>
        </div>

        <!-- Canvas -->
        <div class="h-56 bg-slate-950 rounded-xl border border-cyan-500/40 flex items-center justify-center relative overflow-hidden">
          <canvas id="atom-canvas" width="240" height="200"></canvas>
        </div>

        <!-- Analysis & Live Calculations -->
        <div class="bg-slate-900/80 p-4 rounded-xl border border-slate-700 space-y-3 font-mono text-xs">
          <div class="flex justify-between border-b border-slate-800 pb-1">
            <span class="text-slate-400">Atomic Number (Z):</span><span id="z-res" class="text-cyan-300 font-bold">11</span>
          </div>
          <div class="flex justify-between border-b border-slate-800 pb-1">
            <span class="text-slate-400">Mass Number (A):</span><span id="a-res" class="text-purple-300 font-bold">23</span>
          </div>
          <div class="flex justify-between border-b border-slate-800 pb-1">
            <span class="text-slate-400">Net Ionic Charge:</span><span id="charge-res" class="text-emerald-400 font-bold">+1 (Cation)</span>
          </div>
          <div class="flex justify-between border-b border-slate-800 pb-1">
            <span class="text-slate-400">Electron Shells:</span><span id="shells-res" class="text-amber-300 font-bold">K:2, L:8, M:0</span>
          </div>
          <button onclick="awardAtomScore()" class="w-full mt-2 py-2 bg-cyan-400 hover:bg-cyan-300 text-slate-950 font-bold rounded-lg font-sans">
            Verify Atom Assembly (+25 Pts)
          </button>
        </div>
      </div>
    </section>

    <!-- TAB 2: Formula Forge -->
    <section id="sec-formula" class="scifi-card p-6 border-cyan-500/30 space-y-6 hidden">
      <div class="flex justify-between items-center">
        <div>
          <h2 class="text-lg font-bold text-cyan-300">Formula Forge — Ionic Compound Synthesis</h2>
          <p class="text-xs text-slate-400">Combines cations and anions into electrically neutral compounds with duplicate prevention.</p>
        </div>
        <span class="text-xs font-mono px-2.5 py-1 bg-amber-950 text-amber-300 border border-amber-500/40 rounded">Unique Reward: +50 Pts</span>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label class="text-xs font-bold text-cyan-400 uppercase tracking-wider">1. Select Cation (+ Charge)</label>
          <div class="grid grid-cols-3 gap-2 mt-2" id="cation-grid"></div>
        </div>
        <div>
          <label class="text-xs font-bold text-purple-400 uppercase tracking-wider">2. Select Anion (- Charge)</label>
          <div class="grid grid-cols-3 gap-2 mt-2" id="anion-grid"></div>
        </div>
      </div>

      <!-- Forge Display -->
      <div class="bg-slate-950 p-6 rounded-xl border-2 border-cyan-400 text-center space-y-2">
        <div class="text-xs text-slate-400 uppercase tracking-widest font-bold">Synthesized Compound</div>
        <div id="forge-formula" class="text-4xl font-mono font-black text-white">NaCl</div>
        <div id="forge-name" class="text-sm font-bold text-amber-400">Sodium Chloride (Table Salt / Saline API)</div>
        <div id="forge-neutrality" class="text-xs font-mono text-emerald-400">1 × (Na⁺) + 1 × (Cl⁻) = Net 0</div>
      </div>

      <div id="forge-feedback" class="hidden p-3 rounded-lg text-center text-xs font-bold"></div>

      <button onclick="synthesizeCompound()" class="scifi-btn w-full py-3">
        Synthesize Compound (+50 Pts for New Discovery)
      </button>

      <div class="text-xs text-slate-400">
        <span class="font-bold text-slate-300">Synthesized Codex (<span id="codex-count">0</span> compounds):</span>
        <div id="codex-list" class="flex flex-wrap gap-2 mt-2"></div>
      </div>
    </section>

    <!-- TAB 3: Daily Challenge -->
    <section id="sec-daily" class="scifi-card p-6 border-amber-500/40 space-y-6 hidden">
      <div class="flex justify-between items-center">
        <div>
          <h2 class="text-lg font-bold text-amber-300">Daily Cognitive Quest</h2>
          <p class="text-xs text-slate-400">Deterministic challenge generated from device calendar date. One completion bonus (+250 Pts) per calendar day.</p>
        </div>
        <span id="daily-date-badge" class="text-xs font-mono px-2.5 py-1 bg-slate-900 border border-slate-700 text-amber-400 rounded"></span>
      </div>

      <div id="daily-status-banner" class="p-4 rounded-xl border flex items-center justify-between">
        <div>
          <div id="daily-status-title" class="font-bold text-sm">Today's Quest Available</div>
          <div id="daily-status-desc" class="text-xs text-slate-400">Complete the sequence recall below to earn +250 Pts.</div>
        </div>
        <div id="daily-badge" class="px-3 py-1 text-xs font-bold rounded-full bg-amber-500/20 text-amber-300">Available</div>
      </div>

      <div class="bg-slate-950 p-6 rounded-xl border border-cyan-500/30 text-center space-y-4">
        <div class="text-xs text-cyan-300 font-bold" id="daily-prompt">Tap the orbital quantum nodes in the exact sequence:</div>
        <div class="grid grid-cols-2 gap-4 max-w-xs mx-auto" id="daily-nodes">
          <button onclick="tapDailyNode(0)" class="h-20 bg-slate-900 hover:bg-slate-800 border border-cyan-400 rounded-xl font-bold text-cyan-300 text-base" id="dnode-0">H (1)</button>
          <button onclick="tapDailyNode(1)" class="h-20 bg-slate-900 hover:bg-slate-800 border border-cyan-400 rounded-xl font-bold text-cyan-300 text-base" id="dnode-1">C (6)</button>
          <button onclick="tapDailyNode(2)" class="h-20 bg-slate-900 hover:bg-slate-800 border border-cyan-400 rounded-xl font-bold text-cyan-300 text-base" id="dnode-2">N (7)</button>
          <button onclick="tapDailyNode(3)" class="h-20 bg-slate-900 hover:bg-slate-800 border border-cyan-400 rounded-xl font-bold text-cyan-300 text-base" id="dnode-3">O (8)</button>
        </div>
        <div class="text-xs text-slate-400 font-mono" id="daily-progress">Taps: 0 / 4</div>
      </div>
    </section>

    <!-- TAB 4: Spaced Review -->
    <section id="sec-review" class="scifi-card p-6 border-cyan-500/30 space-y-6 hidden">
      <div>
        <h2 class="text-lg font-bold text-cyan-300">Spaced Repetition Review Queue</h2>
        <p class="text-xs text-slate-400">Adaptive interval active recall. Answer prompts before revealing the scientific formula.</p>
      </div>

      <div class="space-y-4" id="review-cards-container"></div>
    </section>

    <!-- TAB 5: B.Pharm Syllabus -->
    <section id="sec-syllabus" class="scifi-card p-6 border-cyan-500/30 space-y-6 hidden">
      <div class="flex justify-between items-center">
        <div>
          <h2 class="text-lg font-bold text-emerald-300">Pharmacy Council of India (PCI) Foundation Mapping</h2>
          <p class="text-xs text-slate-400">Aligned with PCI revised B.Pharm curriculum as per NEP 2020 (Implementation session 2026–27).</p>
        </div>
      </div>

      <div class="space-y-3 font-sans text-xs" id="syllabus-container"></div>
    </section>
  </div>

  <script>
    // State
    let score = parseInt(localStorage.getItem('nv_score') || '0');
    let synthesized = new Set(JSON.parse(localStorage.getItem('nv_synth') || '[]'));
    let dailyCompleted = localStorage.getItem('nv_daily_' + getTodayDate()) === 'true';

    function getTodayDate() {
      const d = new Date();
      return d.toISOString().split('T')[0];
    }

    function saveState() {
      localStorage.setItem('nv_score', score);
      localStorage.setItem('nv_synth', JSON.stringify(Array.from(synthesized)));
      document.getElementById('score-display').innerText = score;
    }

    document.getElementById('score-display').innerText = score;
    document.getElementById('daily-date-badge').innerText = getTodayDate();

    // Tabs
    function switchTab(tabId) {
      ['atom', 'formula', 'daily', 'review', 'syllabus'].forEach(t => {
        document.getElementById('sec-' + t).classList.add('hidden');
        document.getElementById('tab-' + t).className = 'tab-btn px-4 py-2 rounded-lg font-bold text-sm bg-slate-800 text-slate-300';
      });
      document.getElementById('sec-' + tabId).classList.remove('hidden');
      document.getElementById('tab-' + tabId).className = 'tab-btn px-4 py-2 rounded-lg font-bold text-sm bg-cyan-500 text-slate-950';
    }

    // Atom Canvas
    function updateAtom() {
      const p = parseInt(document.getElementById('p-slider').value);
      const n = parseInt(document.getElementById('n-slider').value);
      const e = parseInt(document.getElementById('e-slider').value);
      document.getElementById('p-val').innerText = p;
      document.getElementById('n-val').innerText = n;
      document.getElementById('e-val').innerText = e;
      document.getElementById('z-res').innerText = p;
      document.getElementById('a-res').innerText = p + n;
      const charge = p - e;
      document.getElementById('charge-res').innerText = (charge > 0 ? '+' + charge + ' (Cation)' : (charge < 0 ? charge + ' (Anion)' : '0 (Neutral)'));
      const k = Math.min(2, e);
      const l = Math.min(8, Math.max(0, e - 2));
      const m = Math.max(0, e - 10);
      document.getElementById('shells-res').innerText = 'K:' + k + ', L:' + l + ', M:' + m;

      const canvas = document.getElementById('atom-canvas');
      const ctx = canvas.getContext('2d');
      ctx.clearRect(0, 0, 240, 200);
      const cx = 120, cy = 100;
      // Orbit rings
      ctx.strokeStyle = '#334155';
      ctx.lineWidth = 1.5;
      ctx.beginPath(); ctx.arc(cx, cy, 35, 0, Math.PI * 2); ctx.stroke();
      if (e > 2) { ctx.beginPath(); ctx.arc(cx, cy, 60, 0, Math.PI * 2); ctx.stroke(); }
      if (e > 10) { ctx.beginPath(); ctx.arc(cx, cy, 85, 0, Math.PI * 2); ctx.stroke(); }

      // Nucleus
      ctx.fillStyle = '#00E5FF';
      ctx.beginPath(); ctx.arc(cx - 4, cy - 2, 8, 0, Math.PI * 2); ctx.fill();
      ctx.fillStyle = '#C084FC';
      ctx.beginPath(); ctx.arc(cx + 4, cy + 2, 7, 0, Math.PI * 2); ctx.fill();

      // Electrons
      ctx.fillStyle = '#FBBF24';
      for (let i = 0; i < k; i++) {
        const ang = (i * Math.PI) + (Date.now() / 2000);
        ctx.beginPath(); ctx.arc(cx + 35 * Math.cos(ang), cy + 35 * Math.sin(ang), 4, 0, Math.PI * 2); ctx.fill();
      }
      for (let i = 0; i < l; i++) {
        const ang = (i * (Math.PI / 4)) - (Date.now() / 2500);
        ctx.beginPath(); ctx.arc(cx + 60 * Math.cos(ang), cy + 60 * Math.sin(ang), 3.5, 0, Math.PI * 2); ctx.fill();
      }
    }
    setInterval(updateAtom, 80);

    function awardAtomScore() {
      score += 25;
      saveState();
      alert('Atomic assembly verified! +25 Points awarded.');
    }

    // Formula Forge Data
    const cations = [
      { sym: 'Na', formula: 'Na⁺', charge: 1 },
      { sym: 'Mg', formula: 'Mg²⁺', charge: 2 },
      { sym: 'Al', formula: 'Al³⁺', charge: 3 },
      { sym: 'Ca', formula: 'Ca²⁺', charge: 2 },
      { sym: 'K', formula: 'K⁺', charge: 1 },
      { sym: 'NH4', formula: 'NH₄⁺', charge: 1, poly: true }
    ];
    const anions = [
      { sym: 'Cl', formula: 'Cl⁻', charge: -1 },
      { sym: 'O', formula: 'O²⁻', charge: -2 },
      { sym: 'SO4', formula: 'SO₄²⁻', charge: -2, poly: true },
      { sym: 'CO3', formula: 'CO₃²⁻', charge: -2, poly: true },
      { sym: 'OH', formula: 'OH⁻', charge: -1, poly: true },
      { sym: 'NO3', formula: 'NO₃⁻', charge: -1, poly: true }
    ];

    let selCat = cations[0];
    let selAn = anions[0];

    function gcd(a, b) { return b === 0 ? a : gcd(b, a % b); }

    function updateForge() {
      const g = gcd(selCat.charge, Math.abs(selAn.charge));
      const cCount = Math.abs(selAn.charge) / g;
      const aCount = selCat.charge / g;
      const sub = ['₀','₁','₂','₃','₄','₅'];
      const cStr = cCount === 1 ? selCat.sym : (selCat.poly ? '(' + selCat.sym + ')' + cCount : selCat.sym + cCount);
      const aStr = aCount === 1 ? selAn.sym : (selAn.poly ? '(' + selAn.sym + ')' + aCount : selAn.sym + aCount);
      const formula = cStr + aStr;
      document.getElementById('forge-formula').innerText = formula;
      document.getElementById('forge-neutrality').innerText = cCount + ' × (' + selCat.formula + ') + ' + aCount + ' × (' + selAn.formula + ') = Net 0';
    }

    function initForgeGrid() {
      const cGrid = document.getElementById('cation-grid');
      cations.forEach(c => {
        const b = document.createElement('button');
        b.className = 'p-2 rounded bg-slate-900 border border-cyan-500/40 font-mono text-cyan-300 font-bold text-xs hover:bg-cyan-950';
        b.innerText = c.formula;
        b.onclick = () => { selCat = c; updateForge(); };
        cGrid.appendChild(b);
      });
      const aGrid = document.getElementById('anion-grid');
      anions.forEach(a => {
        const b = document.createElement('button');
        b.className = 'p-2 rounded bg-slate-900 border border-purple-500/40 font-mono text-purple-300 font-bold text-xs hover:bg-purple-950';
        b.innerText = a.formula;
        b.onclick = () => { selAn = a; updateForge(); };
        aGrid.appendChild(b);
      });
      renderCodex();
      updateForge();
    }
    initForgeGrid();

    function synthesizeCompound() {
      const f = document.getElementById('forge-formula').innerText.trim();
      const fb = document.getElementById('forge-feedback');
      fb.classList.remove('hidden');
      if (synthesized.has(f)) {
        fb.className = 'p-3 rounded-lg text-center text-xs font-bold bg-amber-950 text-amber-300 border border-amber-500/40';
        fb.innerText = f + ' is already recorded in your Codex! No duplicate score awarded.';
      } else {
        synthesized.add(f);
        score += 50;
        saveState();
        renderCodex();
        fb.className = 'p-3 rounded-lg text-center text-xs font-bold bg-emerald-950 text-emerald-300 border border-emerald-500/40';
        fb.innerText = 'New compound forged: ' + f + '! +50 Points awarded!';
      }
    }

    function renderCodex() {
      document.getElementById('codex-count').innerText = synthesized.size;
      const cl = document.getElementById('codex-list');
      cl.innerHTML = '';
      synthesized.forEach(f => {
        const span = document.createElement('span');
        span.className = 'px-2 py-1 rounded bg-slate-900 border border-cyan-400 font-mono text-cyan-300 text-xs font-bold';
        span.innerText = f;
        cl.appendChild(span);
      });
    }

    // Daily Challenge
    const dailySeq = [0, 3, 1, 2];
    let userDailyTaps = [];

    function updateDailyUI() {
      const sb = document.getElementById('daily-status-banner');
      const st = document.getElementById('daily-status-title');
      const sd = document.getElementById('daily-status-desc');
      const bd = document.getElementById('daily-badge');
      if (dailyCompleted) {
        sb.className = 'p-4 rounded-xl border border-emerald-500/40 bg-emerald-950/30 flex items-center justify-between';
        st.innerText = "Today's Quest Completed!";
        st.className = 'font-bold text-sm text-emerald-300';
        sd.innerText = 'You claimed the +250 Pts reward for ' + getTodayDate() + '.';
        bd.className = 'px-3 py-1 text-xs font-bold rounded-full bg-emerald-500/20 text-emerald-300';
        bd.innerText = 'Claimed (+250 Pts)';
      }
    }
    updateDailyUI();

    function tapDailyNode(idx) {
      userDailyTaps.push(idx);
      document.getElementById('daily-progress').innerText = 'Taps: ' + userDailyTaps.length + ' / 4';
      const cur = userDailyTaps.length - 1;
      if (userDailyTaps[cur] !== dailySeq[cur]) {
        alert('Sequence mismatch. Resetting attempt.');
        userDailyTaps = [];
        document.getElementById('daily-progress').innerText = 'Taps: 0 / 4';
        return;
      }
      if (userDailyTaps.length === 4) {
        if (!dailyCompleted) {
          dailyCompleted = true;
          localStorage.setItem('nv_daily_' + getTodayDate(), 'true');
          score += 250;
          saveState();
          updateDailyUI();
          alert('Daily Challenge completed! +250 Points awarded!');
        } else {
          alert('Challenge completed! (Today bonus was already claimed).');
        }
        userDailyTaps = [];
        document.getElementById('daily-progress').innerText = 'Taps: 0 / 4';
      }
    }

    // Spaced Review Items
    const reviewItems = [
      { id: 'rev_z', title: 'Atomic Number (Z)', q: 'What fundamental nuclear subatomic particle defines Atomic Number?', formula: 'Z = p⁺', summary: 'Atomic number equals proton count in the atomic nucleus.' },
      { id: 'rev_a', title: 'Mass Number (A)', q: 'How is total atomic mass number calculated from nucleons?', formula: 'A = p⁺ + n⁰', summary: 'Sum of total protons and neutrons in the nucleus.' },
      { id: 'rev_octet', title: 'Octet Rule', q: 'How many valence electrons do main group elements seek for stability?', formula: 'Valence e⁻ = 8', summary: 'Atoms share or transfer electrons to achieve noble-gas configuration.' },
      { id: 'rev_mole', title: 'Mole Concept', q: 'What is the relationship between mass, molar mass, and moles?', formula: 'n = m / M', summary: 'Amount of substance in moles equals mass in grams divided by molar mass.' },
      { id: 'rev_ph', title: 'pH Scale', q: 'What is the mathematical equation for Sørensen pH?', formula: 'pH = -log₁₀[H⁺]', summary: 'Negative decimal logarithm of hydrogen ion activity.' },
      { id: 'rev_dilution', title: 'Dilution Equation', q: 'What equation governs the conservation of solute upon dilution?', formula: 'C₁V₁ = C₂V₂', summary: 'Initial concentration × volume equals final concentration × volume.' },
      { id: 'rev_saline', title: 'Normal Saline (IV)', q: 'What is the standard isotonic sodium chloride concentration in pharmacopeia?', formula: '0.9% w/v NaCl (9 g/L)', summary: 'Isotonic fluid matching human plasma osmolarity.' }
    ];

    function renderReview() {
      const c = document.getElementById('review-cards-container');
      c.innerHTML = '';
      reviewItems.forEach((item, i) => {
        const div = document.createElement('div');
        div.className = 'scifi-card p-4 border-slate-700 space-y-3';
        div.innerHTML = \`
          <div class="flex justify-between items-center">
            <h3 class="font-bold text-sm text-cyan-300">\${item.title}</h3>
            <span class="text-xs font-mono text-slate-400">Card \${i+1}/\${reviewItems.length}</span>
          </div>
          <p class="text-xs text-slate-300">\${item.q}</p>
          <div id="ans-\${item.id}" class="hidden p-3 bg-slate-950 rounded-lg border border-cyan-500/40">
            <div class="font-mono text-emerald-400 font-bold text-sm">\${item.formula}</div>
            <div class="text-xs text-slate-400 mt-1">\${item.summary}</div>
          </div>
          <div class="flex space-x-2">
            <button onclick="document.getElementById('ans-\${item.id}').classList.toggle('hidden')" class="px-3 py-1.5 bg-cyan-950 hover:bg-cyan-900 border border-cyan-500 text-cyan-300 text-xs font-bold rounded">Reveal Formula</button>
            <button onclick="score+=10; saveState(); alert('Recalled! +10 Pts');" class="px-3 py-1.5 bg-emerald-950 hover:bg-emerald-900 border border-emerald-500 text-emerald-300 text-xs font-bold rounded">Remembered (+10 Pts)</button>
          </div>
        \`;
        c.appendChild(div);
      });
    }
    renderReview();

    // Syllabus
    const syllabus = [
      { code: 'BP106T-U1', status: 'VERIFIED', subj: 'Pharmaceutical Inorganic and Analytical Chemistry', topic: 'Impurities in Pharmaceutical Substances & Limit Tests', desc: 'Sources of impurities in active pharmaceutical ingredients (APIs); limit tests for chloride, sulfate, iron, and heavy metals.' },
      { code: 'BP106T-U2', status: 'VERIFIED', subj: 'Pharmaceutical Inorganic and Analytical Chemistry', topic: 'Volumetric Analysis: Acid-Base Titration & Neutralization Indicators', desc: 'Theories of acid-base indicators, neutralization curves, primary/secondary standards, and volumetric assay of NaOH and HCl.' },
      { code: 'BP106T-U3', status: 'VERIFIED', subj: 'Pharmaceutical Inorganic and Analytical Chemistry', topic: 'Gastrointestinal Agents: Antacids, Acidifiers & Protectives', desc: 'Mechanism, preparation, and assay of inorganic antacids: Aluminium hydroxide gel, Magnesium hydroxide, Calcium carbonate.' },
      { code: 'BP106T-U4', status: 'VERIFIED', subj: 'Pharmaceutical Inorganic and Analytical Chemistry', topic: 'Major Intra- & Extracellular Electrolytes & Isotonic Solutions', desc: 'Physiological functions of Na⁺, K⁺, Ca²⁺, Cl⁻; formulation of ORS and 0.9% w/v isotonic sodium chloride saline.' },
      { code: 'BP202T-U1', status: 'PROVISIONAL', subj: 'Pharmaceutical Organic Chemistry I', topic: 'IUPAC Nomenclature, Carbon Hybridization & Structural Isomerism', desc: 'Classification of organic compounds, IUPAC nomenclature rules, functional group priority, and isomerism.' },
      { code: 'BP302T-U1', status: 'PROVISIONAL', subj: 'Physical Pharmaceutics I', topic: 'pH, Buffers, Tonicity & Physicochemical Principles', desc: 'Sørensen pH scale, Henderson-Hasselbalch equation, buffer capacity, and isotonicity adjustments for parenterals.' }
    ];

    function renderSyllabus() {
      const sc = document.getElementById('syllabus-container');
      syllabus.forEach(s => {
        const div = document.createElement('div');
        div.className = 'scifi-card p-3 border-slate-700';
        div.innerHTML = \`
          <div class="flex justify-between items-center">
            <span class="font-mono text-cyan-400 font-bold">\${s.code}</span>
            <span class="\${s.status === 'VERIFIED' ? 'text-emerald-400' : 'text-amber-400'} font-black text-[10px] tracking-wider">\${s.status}</span>
          </div>
          <div class="font-bold text-white text-xs mt-1">\${s.topic}</div>
          <div class="text-[11px] text-purple-300 font-semibold">\${s.subj}</div>
          <p class="text-[11px] text-slate-400 mt-1">\${s.desc}</p>
        \`;
        sc.appendChild(div);
      });
    }
    renderSyllabus();
  </script>
</body>
</html>`;

const server = http.createServer((req, res) => {
  if (req.url === '/download-apk') {
    if (fs.existsSync(APK_PATH)) {
      const stat = fs.statSync(APK_PATH);
      res.writeHead(200, {
        'Content-Type': 'application/vnd.android.package-archive',
        'Content-Length': stat.size,
        'Content-Disposition': 'attachment; filename="NeuroVault-debug.apk"'
      });
      const readStream = fs.createReadStream(APK_PATH);
      readStream.pipe(res);
      return;
    } else {
      res.writeHead(404, { 'Content-Type': 'text/plain' });
      res.end('APK not found. Please compile the Android project first.');
      return;
    }
  }

  if (req.url === '/health') {
    res.writeHead(200, { 'Content-Type': 'application/json' });
    res.end(JSON.stringify({ status: 'ok', timestamp: new Date().toISOString() }));
    return;
  }

  res.writeHead(200, { 'Content-Type': 'text/html; charset=utf-8' });
  res.end(htmlContent);
});

server.listen(PORT, HOST, () => {
  console.log(`NeuroVault server running on http://${HOST}:${PORT}`);
});
