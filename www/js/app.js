/* =========================================================
   Baba Cosmetics — App v2
   Live products, prices & images from www.babacosmetics.com (Shopify)
   ========================================================= */
(function () {
'use strict';

const STORE = 'https://www.babacosmetics.com';
const LOGO_LOCAL = 'assets/logo.png';
const LOGO_REMOTE = 'https://babacosmetics.com/cdn/shop/files/LOGO_ecdffdbf-7ac6-4045-8a38-024980327e99_500x.png?v=1759939619';
const WHATSAPP = '923251370000';
const FREE_DELIVERY = 3000;
const BANNERS = [
  { img: 'https://babacosmetics.com/cdn/shop/files/11-11-sale-banner-mobile.jpg?v=1793266378&width=900', go: '#/all' },
  { img: 'https://babacosmetics.com/cdn/shop/files/Web_Banner_4_581x770_5c1a81b8-f582-4440-b72e-5008c5328d4a.webp?v=1783575070&width=900', go: '#/c/serums' },
  { img: 'https://babacosmetics.com/cdn/shop/files/Web_Banner_5_581x770_f7b314a1-7838-4db8-a35e-db3c31de54cd.webp?v=1783575069&width=900', go: '#/c/face-wash-range' },
  { img: 'https://babacosmetics.com/cdn/shop/files/Web_Banner_6_581x770_8a52695f-3472-4bd6-abc3-06f39a039fc2.webp?v=1783575069&width=900', go: '#/c/bleach' }
];
const WIDE_BANNER = { img: 'https://babacosmetics.com/cdn/shop/files/parlor-pack-banner-desktop.jpg?v=1793099209&width=1200', go: '#/c/parlor-pack' };
const ANNOUNCE = ['11/11 SALE IS ON!', 'FREE Delivery on shopping of Rs. 3,000+', 'Cash on Delivery all over Pakistan', '100% Original Baba Cosmetics products'];

const isBundle = p => /bundle/i.test(p.product_type) || /bundle/i.test(p.title) || /(glow|care|starter|polish) pack\b/i.test(p.title);
const CATS = [
  { h: 'bleach',             n: 'Bleach',        m: /bleach/i },
  { h: 'face-wash-range',    n: 'Face Wash',     m: /face ?wash/i },
  { h: 'serums',             n: 'Serums',        m: /serum/i },
  { h: 'urgent-facial',      n: 'Urgent Facial', m: /facial/i },
  { h: 'bundle',             n: 'Bundles',       f: isBundle },
  { h: 'parlor-pack',        n: 'Parlor Pack',   m: /parlou?r|500 ?ml/i },
  { h: 'sunblock',           n: 'Sunblock',      m: /sun ?block/i },
  { h: 'scrub',              n: 'Scrub',         m: /scrub/i },
  { h: 'men-glow-facewash',  n: 'Men Glow',      m: /\bmen/i },
  { h: 'hair-removal-cream', n: 'Hair Removal',  m: /hair remov|remover/i },
  { h: 'skin-polish',        n: 'Skin Polish',   m: /polish/i },
  { h: 'bb-cream',           n: 'BB Cream',      m: /\bbb\b/i },
  { h: 'finger-wax',         n: 'Finger Wax',    m: /\bwax/i },
  { h: 'sachet',             n: 'Sachets',       m: /sachet|\(5 ?packs?\)/i }
];
const HINTS = ['Search "Vitamin C Serum"', 'Search "Parlor Pack"', 'Search "Coffee Facewash"', 'Search "Sun Block"', 'Search "Urgent Facial"', 'Search "Bleach Cream"'];

/* ---------- icons ---------- */
const I = {
  heart: '<svg viewBox="0 0 24 24"><path d="M12 20s-7.5-4.6-7.5-10.2A4.3 4.3 0 0 1 12 7.4a4.3 4.3 0 0 1 7.5 2.4C19.5 15.4 12 20 12 20z"/></svg>',
  bag: '<svg viewBox="0 0 24 24"><path d="M5 8h14l-1 12H6z"/><path d="M9 8V6.5a3 3 0 0 1 6 0V8"/><path d="M12 11.5v5M9.5 14h5"/></svg>',
  chev: '<svg viewBox="0 0 24 24"><path d="M9 6l6 6-6 6"/></svg>',
  down: '<svg viewBox="0 0 24 24"><path d="M6 9l6 6 6-6"/></svg>',
  trash: '<svg viewBox="0 0 24 24"><path d="M5 7h14M10 7V5h4v2M7 7l1 13h8l1-13"/></svg>',
  truck: '<svg viewBox="0 0 24 24"><path d="M3 6h11v10H3zM14 10h4l3 3v3h-7"/><circle cx="7" cy="17.5" r="1.8"/><circle cx="17" cy="17.5" r="1.8"/></svg>',
  cash: '<svg viewBox="0 0 24 24"><rect x="3" y="6" width="18" height="12" rx="2"/><circle cx="12" cy="12" r="2.6"/><path d="M6.5 9.5v.01M17.5 14.5v.01"/></svg>',
  shield: '<svg viewBox="0 0 24 24"><path d="M12 3l7 3v5c0 4.5-3 8-7 10-4-2-7-5.5-7-10V6z"/><path d="M9 12l2 2 4-4"/></svg>',
  spark: '<svg viewBox="0 0 24 24"><path d="M12 3l1.8 5.2L19 10l-5.2 1.8L12 17l-1.8-5.2L5 10l5.2-1.8z"/><path d="M19 16l.7 1.8 1.8.7-1.8.7L19 21l-.7-1.8-1.8-.7 1.8-.7z"/></svg>',
  wa: '<svg viewBox="0 0 24 24"><path d="M4 20l1.2-3.8A8 8 0 1 1 8 19z"/><path d="M9 9.5c0 3 2.5 5.5 5.5 5.5l1.2-1.4-2-1-1 .8a4 4 0 0 1-2.1-2.1l.8-1-1-2z"/></svg>',
  box: '<svg viewBox="0 0 24 24"><path d="M3.5 7.5L12 3l8.5 4.5v9L12 21l-8.5-4.5z"/><path d="M3.5 7.5L12 12l8.5-4.5M12 12v9"/></svg>',
  globe: '<svg viewBox="0 0 24 24"><circle cx="12" cy="12" r="8.5"/><path d="M3.5 12h17M12 3.5c2.5 2.6 2.5 14.4 0 17M12 3.5c-2.5 2.6-2.5 14.4 0 17"/></svg>',
  mail: '<svg viewBox="0 0 24 24"><rect x="3" y="5.5" width="18" height="13" rx="2"/><path d="M3.5 7l8.5 6 8.5-6"/></svg>',
  pin: '<svg viewBox="0 0 24 24"><path d="M12 21s-6.5-6-6.5-11a6.5 6.5 0 0 1 13 0c0 5-6.5 11-6.5 11z"/><circle cx="12" cy="10" r="2.3"/></svg>',
  grid: '<svg viewBox="0 0 24 24"><rect x="4" y="4" width="6.5" height="6.5" rx="1.8"/><rect x="13.5" y="4" width="6.5" height="6.5" rx="1.8"/><rect x="4" y="13.5" width="6.5" height="6.5" rx="1.8"/><rect x="13.5" y="13.5" width="6.5" height="6.5" rx="1.8"/></svg>',
  clock: '<svg viewBox="0 0 24 24"><circle cx="12" cy="12" r="8.5"/><path d="M12 7.5V12l3 2"/></svg>',
  search: '<svg viewBox="0 0 24 24"><circle cx="11" cy="11" r="6.5"/><path d="M20 20l-4.2-4.2"/></svg>',
  mic: '<svg viewBox="0 0 24 24"><rect x="9" y="3" width="6" height="11" rx="3"/><path d="M5.5 11a6.5 6.5 0 0 0 13 0M12 17.5V21"/></svg>',
  share: '<svg viewBox="0 0 24 24"><circle cx="18" cy="5.5" r="2.5"/><circle cx="6" cy="12" r="2.5"/><circle cx="18" cy="18.5" r="2.5"/><path d="M8.2 10.8l7.6-4M8.2 13.2l7.6 4"/></svg>'
};

/* ---------- helpers ---------- */
const $ = (s, r = document) => r.querySelector(s);
const $$ = (s, r = document) => Array.from(r.querySelectorAll(s));
const esc = s => String(s == null ? '' : s).replace(/[&<>"']/g, c => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c]));
const rs = n => 'Rs. ' + Math.round(n).toLocaleString('en-US');
const num = v => parseFloat(v) || 0;
const store = {
  get(k, d) { try { const v = localStorage.getItem('bc_' + k); return v ? JSON.parse(v) : d; } catch (e) { return d; } },
  set(k, v) { try { localStorage.setItem('bc_' + k, JSON.stringify(v)); } catch (e) {} }
};
const Cap = window.Capacitor;
const Plug = n => (Cap && Cap.Plugins && Cap.Plugins[n]) || null;
const isNative = !!(Cap && Cap.isNativePlatform && Cap.isNativePlatform());

function imgUrl(src, w) {
  if (!src) return '';
  if (!/cdn\.shopify\.com|\/cdn\/shop\//.test(src)) return src;
  return src + (src.includes('?') ? '&' : '?') + 'width=' + w;
}
function firstImg(p, w = 500) { return imgUrl(p.images && p.images[0] && (p.images[0].src || p.images[0]), w); }
function priceOf(p) {
  const v = (p.variants || [])[0] || {};
  const price = num(v.price), cmp = num(v.compare_at_price);
  return { price, cmp: cmp > price ? cmp : 0, off: cmp > price ? Math.round((1 - price / cmp) * 100) : 0 };
}
const available = p => (p.variants || []).some(v => v.available !== false);
window.bcImgErr = function (el) {
  el.onerror = null; el.style.visibility = 'hidden';
  const box = el.parentElement;
  if (box && !box.querySelector('.ph-txt')) box.insertAdjacentHTML('beforeend', '<div class="ph-txt">' + esc(el.alt || 'Baba Cosmetics') + '</div>');
};

function toast(msg) {
  const t = $('#toast'); t.textContent = msg; t.classList.add('on');
  clearTimeout(toast._t); toast._t = setTimeout(() => t.classList.remove('on'), 2000);
}
function openExternal(url) {
  const B = Plug('Browser');
  if (B && /^https?:/.test(url) && !/wa\.me|whatsapp/.test(url)) { B.open({ url, toolbarColor: '#A3154B' }).catch(() => { location.href = url; }); return; }
  if (isNative) { location.href = url; return; }       // Android hands wa.me / mailto to the right app
  const w = window.open(url, '_blank'); if (!w) location.href = url;
}

/* ---------- logo ---------- */
function setLogo(img, word) {
  if (!img) return;
  img.style.display = 'none';
  img.onload = () => { img.style.display = ''; if (word) word.style.display = 'none'; };
  img.onerror = () => {
    if (img.dataset.tried) return;
    img.dataset.tried = '1'; img.src = LOGO_REMOTE;
  };
  img.src = LOGO_LOCAL;
}

/* =========================================================
   DATA
   ========================================================= */
const S = {
  products: store.get('products', []),
  collections: store.get('collections', []),
  colCache: {},
  live: false,
  cart: store.get('cart', []),
  wish: store.get('wish', []),
  recent: store.get('recent', [])
};

async function getJSON(url, ms = 12000) {
  const ctl = 'AbortController' in window ? new AbortController() : null;
  const timer = setTimeout(() => ctl && ctl.abort(), ms);
  try {
    const r = await fetch(url, { signal: ctl ? ctl.signal : undefined, headers: { Accept: 'application/json' } });
    if (!r.ok) throw new Error('HTTP ' + r.status);
    return await r.json();
  } finally { clearTimeout(timer); }
}
async function fetchAllProducts() {
  let all = [], page = 1;
  while (page < 10) {
    const d = await getJSON(`${STORE}/products.json?limit=250&page=${page}`);
    const list = (d && d.products) || [];
    all = all.concat(list);
    if (list.length < 250) break;
    page++;
  }
  return all.map(slim);
}
function slim(p) {
  return {
    id: p.id, handle: p.handle, title: p.title, product_type: p.product_type || '', tags: p.tags || [],
    body_html: p.body_html || '', created_at: p.created_at || '',
    images: (p.images || []).map(i => ({ src: i.src })),
    variants: (p.variants || []).map(v => ({ id: v.id, title: v.title, price: v.price, compare_at_price: v.compare_at_price, available: v.available !== false }))
  };
}
async function loadData() {
  const jobs = [
    fetchAllProducts().then(list => {
      if (list.length) { S.products = list; S.live = true; store.set('products', list); }
    }),
    getJSON(`${STORE}/collections.json?limit=250`).then(d => {
      S.collections = ((d && d.collections) || []).map(c => ({ handle: c.handle, title: c.title, count: c.products_count, image: c.image && c.image.src }));
      store.set('collections', S.collections);
    })
  ];
  await Promise.allSettled(jobs);
  if (!S.products.length) S.products = (window.BC_SEED && window.BC_SEED.products) || [];
}
async function getCollection(handle) {
  if (S.colCache[handle]) return S.colCache[handle];
  try {
    const d = await getJSON(`${STORE}/collections/${encodeURIComponent(handle)}/products.json?limit=250`, 10000);
    const list = ((d && d.products) || []).map(slim);
    // merge with richer data we may already hold
    S.colCache[handle] = list;
    return list;
  } catch (e) { return null; }
}
function byHandle(h) { return S.products.find(p => p.handle === h) || Object.values(S.colCache).flat().find(p => p.handle === h); }
function localCat(cat) {
  const c = typeof cat === 'string' ? CATS.find(x => x.h === cat) : cat;
  if (!c) return [];
  return S.products.filter(p => c.f ? c.f(p) : c.m.test(p.title + ' ' + p.product_type + ' ' + (p.tags || []).join(' ')));
}
function catName(h) {
  const c = CATS.find(x => x.h === h); if (c) return c.n;
  const k = S.collections.find(x => x.handle === h); if (k) return k.title;
  return h.replace(/-/g, ' ').replace(/\b\w/g, m => m.toUpperCase());
}
function liveCats() {
  if (!S.collections.length) return CATS.filter(c => localCat(c).length);
  return CATS.filter(c => { const k = S.collections.find(x => x.handle === c.h); return (k && k.count > 0) || localCat(c).length; });
}

/* =========================================================
   CART & WISHLIST
   ========================================================= */
function saveCart() { store.set('cart', S.cart); updBadge(); }
function updBadge(bump) {
  const n = S.cart.reduce((a, i) => a + i.qty, 0), b = $('#cartBadge');
  b.textContent = n > 99 ? '99+' : n; b.classList.toggle('hidden', !n);
  if (bump) { b.classList.remove('bump'); void b.offsetWidth; b.classList.add('bump'); }
}
function addToCart(p, vIdx = 0, qty = 1, silent) {
  const v = p.variants[vIdx] || p.variants[0];
  const key = p.handle + '|' + (v.id || vIdx);
  const ex = S.cart.find(i => i.key === key);
  if (ex) ex.qty += qty;
  else S.cart.push({ key, handle: p.handle, vid: v.id, vtitle: v.title === 'Default Title' ? '' : v.title, title: p.title, price: num(v.price), cmp: num(v.compare_at_price), img: firstImg(p, 300), qty });
  saveCart(); updBadge(true);
  if (!silent) toast('✓ Added to cart');
  haptic();
}
function cartTotal() { return S.cart.reduce((a, i) => a + i.price * i.qty, 0); }
function toggleWish(h, btn) {
  const i = S.wish.indexOf(h);
  if (i > -1) S.wish.splice(i, 1); else S.wish.unshift(h);
  store.set('wish', S.wish);
  const on = S.wish.includes(h);
  $$(`[data-wish="${CSS.escape(h)}"]`).forEach(b => { b.classList.toggle('on', on); b.classList.remove('pop'); void b.offsetWidth; b.classList.add('pop'); });
  toast(on ? '♥ Saved to wishlist' : 'Removed from wishlist');
  haptic();
}
function haptic() { try { navigator.vibrate && navigator.vibrate(12); } catch (e) {} }
function checkout() {
  if (!S.cart.length) return;
  if (S.cart.some(i => !i.vid)) { openExternal(STORE + '/cart'); return; }
  const url = STORE + '/cart/' + S.cart.map(i => i.vid + ':' + i.qty).join(',');
  openExternal(url);
}
function whatsappOrder() {
  let t = 'Assalam o Alaikum! Mujhe ye order karna hai:\n\n';
  S.cart.forEach((i, n) => { t += `${n + 1}. ${i.title}${i.vtitle ? ' (' + i.vtitle + ')' : ''} x ${i.qty} = ${rs(i.price * i.qty)}\n`; });
  t += `\nTotal: ${rs(cartTotal())}\n\nNaam:\nAddress:\nCity:`;
  openExternal(`https://wa.me/${WHATSAPP}?text=${encodeURIComponent(t)}`);
}

/* =========================================================
   COMPONENTS
   ========================================================= */
function card(p) {
  const { price, cmp, off } = priceOf(p), on = S.wish.includes(p.handle), av = available(p);
  return `<div class="pcard" data-p="${esc(p.handle)}">
    <div class="pimg">
      ${off ? `<span class="off">-${off}%</span>` : ''}
      <button class="heart ${on ? 'on' : ''}" data-wish="${esc(p.handle)}" aria-label="Wishlist">${I.heart}</button>
      <img loading="lazy" src="${esc(firstImg(p, 500))}" alt="${esc(p.title)}" onerror="bcImgErr(this)">
      ${av ? `<button class="addc" data-add="${esc(p.handle)}" aria-label="Add to cart">${I.bag}</button>` : '<div class="soldout">Sold out</div>'}
    </div>
    <h4>${esc(p.title)}</h4>
    <div class="price"><b>${rs(price)}</b>${cmp ? `<s>${rs(cmp)}</s>` : ''}</div>
  </div>`;
}
const skCards = n => Array.from({ length: n }, () => '<div class="pcard"><div class="pimg sk" style="border:0"></div><div class="sk" style="height:14px;margin-top:10px;width:90%"></div><div class="sk" style="height:14px;margin-top:6px;width:50%"></div></div>').join('');
function secHead(title, sub, link) {
  return `<div class="sec-h"><h2>${title}${sub ? `<small>${sub}</small>` : ''}</h2>${link ? `<a href="${link}">View all ${I.chev}</a>` : ''}</div>`;
}
function rail(list) { return `<div class="hscroll">${list.map(card).join('')}</div>`; }

/* =========================================================
   TOP BAR
   ========================================================= */
function setTop({ back = false, title = '', search = false }) {
  $('#btnMenu').classList.toggle('hidden', back);
  $('#btnBack').classList.toggle('hidden', !back);
  $('#searchPill').classList.toggle('hidden', !search);
  $('#app').classList.toggle('no-search', !search);
  const t = $('#tbTitle');
  if (title) { t.innerHTML = `<span class="tb-page">${esc(title)}</span>`; }
  else if (!t.querySelector('.tb-word')) {
    t.innerHTML = '<img class="tb-logo" alt="Baba Cosmetics"><span class="tb-word">Baba<em>Cosmetics</em></span>';
    setLogo($('.tb-logo', t), $('.tb-word', t));
  }
}
function setTab(tab) { $$('#bottomnav a').forEach(a => a.classList.toggle('active', a.dataset.tab === tab)); }

/* =========================================================
   VIEWS
   ========================================================= */
let heroTimer = null, hintTimer = null;
const V = {};

V.home = function () {
  setTop({ search: true }); setTab('home');
  const P = S.products;
  const maxOff = Math.max(0, ...P.map(p => priceOf(p).off));
  const offs = [...new Set(P.map(p => priceOf(p).off).filter(Boolean))].sort((a, b) => b - a).slice(0, 4);
  const promoDefs = [
    { h: 'urgent-facial', t: 'Facial Range', g: '' },
    { h: 'serums', t: 'Glow Serums', g: 'g2' },
    { h: 'bleach', t: 'Bleach Creams', g: 'g4' },
    { h: 'face-wash-range', t: 'Face Wash', g: 'g3' }
  ];
  const promos = promoDefs.map(d => {
    const list = localCat(d.h); if (!list.length) return '';
    const o = Math.max(0, ...list.map(p => priceOf(p).off));
    const imgs = list.slice(0, 3).map(p => `<img src="${esc(firstImg(p, 200))}" alt="" onerror="this.remove()">`).join('');
    return `<a class="promo ${d.g}" href="#/c/${d.h}"><h4>${d.t}</h4>${o ? `<div class="flat"><small>UPTO</small>${o}%<small>OFF</small></div>` : `<div class="flat"><small>NEW</small></div>`}<span class="shop">SHOP NOW →</span><div class="imgs">${imgs}</div></a>`;
  }).join('');
  const cats = liveCats();
  const stories = cats.map(c => { const p = localCat(c)[0]; return `<a class="story" href="#/c/${c.h}"><div class="ring"><div>${p ? `<img src="${esc(firstImg(p, 200))}" alt="" onerror="this.remove()">` : ''}</div></div><span>${c.n}</span></a>`; }).join('');
  const trend = ['bundle', 'serums', 'parlor-pack', 'urgent-facial'].map(h => {
    const l = localCat(h); if (!l.length) return '';
    return `<a class="tcard" href="#/c/${h}"><b>${catName(h)}<small>${l.length} products</small></b><img src="${esc(firstImg(l[0], 200))}" alt="" onerror="this.style.visibility='hidden'"></a>`;
  }).join('');
  const bundles = P.filter(isBundle);
  const deals = P.filter(p => priceOf(p).off > 0 && available(p)).sort((a, b) => priceOf(b).off - priceOf(a).off).slice(0, 10);
  const serums = localCat('serums'), fw = localCat('face-wash-range');

  $('#view').innerHTML = `
    <section class="hero">
      <div class="hero-track" id="heroTrack">${BANNERS.map(b => `<a class="hero-slide" href="${b.go}"><div class="ph"><img src="${esc(b.img)}" alt="Baba Cosmetics offer" onerror="bcImgErr(this)"></div></a>`).join('')}</div>
      <div class="hero-dots" id="heroDots">${BANNERS.map((_, i) => `<i class="${i ? '' : 'on'}"></i>`).join('')}</div>
    </section>

    <section class="sec offers">
      <h3>Crazy Offers <span>| UPTO ${maxOff || 30}% OFF</span></h3>
      <div class="row">${(offs.length ? offs : [30, 20, 15]).map(o => `<span>${o}% OFF</span>`).join('')}</div>
    </section>

    <section class="sec" style="padding-top:12px"><div class="hscroll">${promos}</div></section>

    <div class="marquee"><div>${ANNOUNCE.concat(ANNOUNCE).map(a => esc(a)).join('<b>✦</b>')}</div></div>

    <section class="sec">${secHead('Shop by Category', '', '#/cat')}<div class="hscroll stories">${stories}</div></section>

    ${trend ? `<section class="sec">${secHead('Trending Collections')}<div class="tgrid">${trend}</div></section>` : ''}

    ${bundles.length ? `<section class="sec">${secHead('#Bundles Don\'t Miss', 'More products, less price', '#/c/bundle')}${rail(bundles)}</section>` : ''}

    ${deals.length ? `<section class="sec">${secHead('Today\'s Best Deals', 'Biggest discounts right now', '#/all?sort=off')}${rail(deals)}</section>` : ''}

    <section class="sec"><a href="${WIDE_BANNER.go}"><div class="wbanner"><img src="${esc(WIDE_BANNER.img)}" alt="Parlor Pack" onerror="bcImgErr(this)"></div></a></section>

    <section class="sec">${secHead('Best Sellers', 'Customers\' favourites', '#/c/best-sellers')}<div class="pgrid" id="bestGrid">${skCards(4)}</div></section>

    ${serums.length ? `<section class="sec">${secHead('Glow Serums', 'Vitamin C, Gluta, Coffee & more', '#/c/serums')}${rail(serums)}</section>` : ''}
    ${fw.length ? `<section class="sec">${secHead('Face Wash Range', 'Fresh skin every day', '#/c/face-wash-range')}${rail(fw)}</section>` : ''}

    <section class="sec">${secHead('Why Baba Cosmetics?')}
      <div class="trust">
        <div>${I.cash}<b>Cash on Delivery<small>Pay when you receive</small></b></div>
        <div>${I.truck}<b>Free Delivery<small>On orders Rs. 3,000+</small></b></div>
        <div>${I.box}<b>Fast Courier<small>PostEx & TCS</small></b></div>
        <div>${I.shield}<b>100% Original<small>Direct from brand</small></b></div>
      </div>
    </section>

    <div class="foot"><div class="tb-word">Baba<em>Cosmetics</em></div>www.babacosmetics.com</div>`;

  // best sellers (live collection, fallback to deals)
  getCollection('best-sellers').then(list => {
    const g = $('#bestGrid'); if (!g) return;
    const L = (list && list.length ? list : P.slice(0, 8)).slice(0, 8);
    g.innerHTML = L.map(card).join('');
  });

  // hero auto slide
  const tr = $('#heroTrack'), dots = $$('#heroDots i');
  let idx = 0;
  tr.addEventListener('scroll', () => {
    const sl = tr.children[0] ? tr.children[0].offsetWidth + 12 : tr.clientWidth;
    const i = Math.round(tr.scrollLeft / sl);
    if (i !== idx) { idx = i; dots.forEach((d, k) => d.classList.toggle('on', k === i)); }
  }, { passive: true });
  heroTimer = setInterval(() => { if (!document.hidden) { const n = tr.children[(idx + 1) % BANNERS.length]; tr.scrollTo({ left: n.offsetLeft - (tr.clientWidth - n.offsetWidth) / 2, behavior: 'smooth' }); }; }, 4000);

  // rotating search hints
  let h = 0; const el = $('#searchHint');
  hintTimer = setInterval(() => { el.style.opacity = 0; setTimeout(() => { h = (h + 1) % HINTS.length; el.textContent = HINTS[h]; el.style.opacity = 1; }, 300); }, 2600);
};

V.cat = function (handle) {
  setTop({ title: 'Categories' }); setTab('cat');
  const cats = liveCats();
  const cur = handle && cats.find(c => c.h === handle) ? handle : (cats[0] && cats[0].h);
  $('#view').innerHTML = `<div class="catpage">
    <div class="catside">${cats.map(c => { const p = localCat(c)[0]; return `<button data-cat="${c.h}" class="${c.h === cur ? 'on' : ''}">${p ? `<img src="${esc(firstImg(p, 120))}" alt="" onerror="this.style.visibility='hidden'">` : ''}${c.n}</button>`; }).join('')}</div>
    <div class="catmain" id="catMain"></div></div>`;
  const show = h => {
    $$('.catside button').forEach(b => b.classList.toggle('on', b.dataset.cat === h));
    const list = localCat(h);
    const m = $('#catMain');
    const draw = L => { m.innerHTML = `<div class="cat-banner"><h3>${catName(h)}</h3><p>${L.length} products • Free delivery on Rs. 3,000+</p></div><div class="pgrid">${L.map(card).join('') || '<p style="color:var(--muted)">Coming soon</p>'}</div>`; };
    draw(list); m.scrollTop = 0;
    getCollection(h).then(L => { if (L && L.length && $('.catside button.on') && $('.catside button.on').dataset.cat === h) draw(L); });
  };
  $('.catside').addEventListener('click', e => { const b = e.target.closest('[data-cat]'); if (b) { history.replaceState(null, '', '#/cat/' + b.dataset.cat); show(b.dataset.cat); } });
  if (cur) show(cur);
};

function listView(title, sub, list, sortKey) {
  const sorts = [['featured', 'Featured'], ['new', 'Newest'], ['low', 'Price: Low → High'], ['high', 'Price: High → Low'], ['off', 'Biggest Discount']];
  let cur = sortKey || 'featured';
  const sorted = () => {
    const L = list.slice();
    if (cur === 'low') L.sort((a, b) => priceOf(a).price - priceOf(b).price);
    if (cur === 'high') L.sort((a, b) => priceOf(b).price - priceOf(a).price);
    if (cur === 'off') L.sort((a, b) => priceOf(b).off - priceOf(a).off);
    if (cur === 'new') L.sort((a, b) => String(b.created_at).localeCompare(String(a.created_at)));
    return L;
  };
  $('#view').innerHTML = `<div class="listhead"><h1>${esc(title)}</h1><p id="lsub">${esc(sub)}</p></div>
    <div class="sortbar">${sorts.map(s => `<button class="chip ${s[0] === cur ? 'on' : ''}" data-sort="${s[0]}">${s[1]}</button>`).join('')}</div>
    <div class="sec" style="padding-top:4px"><div class="pgrid" id="lgrid"></div></div>`;
  const draw = () => { $('#lgrid').innerHTML = sorted().map(card).join('') || '<p style="color:var(--muted);grid-column:1/-1;text-align:center;padding:40px">No products yet</p>'; };
  draw();
  $('.sortbar').addEventListener('click', e => { const b = e.target.closest('[data-sort]'); if (!b) return; cur = b.dataset.sort; $$('.sortbar .chip').forEach(c => c.classList.toggle('on', c === b)); draw(); });
  return { update(L) { list = L; $('#lsub').textContent = L.length + ' products'; draw(); } };
}

V.c = function (handle) {
  const name = catName(handle);
  setTop({ back: true, title: name }); setTab('cat');
  const local = localCat(handle);
  const lv = listView(name, local.length ? local.length + ' products' : 'Loading…', local);
  if (!local.length) $('#lgrid').innerHTML = skCards(6);
  getCollection(handle).then(L => { if (L && L.length) lv.update(L); else if (!local.length) lv.update([]); });
};

V.all = function (q) {
  setTop({ back: true, title: 'All Products' }); setTab('cat');
  const sort = new URLSearchParams(q || '').get('sort');
  listView(sort === 'off' ? 'Best Deals' : 'All Products', S.products.length + ' products', S.products, sort);
};

V.p = async function (handle) {
  setTop({ back: true, title: '' }); setTab('');
  let p = byHandle(handle);
  if (!p) {
    $('#view').innerHTML = `<div class="pp"><div class="pp-gal sk" style="aspect-ratio:1/1"></div></div>`;
    try { const d = await getJSON(`${STORE}/products/${encodeURIComponent(handle)}.json`); p = slim(d.product); } catch (e) {}
    if (!p) { $('#view').innerHTML = `<div class="empty"><h3>Product not found</h3><p>Ye product abhi available nahi.</p><a class="btn" href="#/home">Go Home</a></div>`; return; }
  }
  let vi = Math.max(0, p.variants.findIndex(v => v.available !== false)), qty = 1;
  const imgs = (p.images.length ? p.images : [{ src: '' }]).map(i => imgUrl(i.src, 900));
  const related = S.products.filter(x => x.handle !== p.handle && (x.product_type && x.product_type === p.product_type)).slice(0, 10);
  const more = related.length ? related : S.products.filter(x => x.handle !== p.handle).slice(0, 10);
  const desc = (p.body_html || '').replace(/<(script|iframe|style)[\s\S]*?<\/\1>/gi, '').replace(/ on\w+="[^"]*"/gi, '');

  const priceHtml = () => {
    const v = p.variants[vi]; const pr = num(v.price), c = num(v.compare_at_price);
    return `<b>${rs(pr)}</b>${c > pr ? `<s>${rs(c)}</s><em>Save ${rs(c - pr)}</em>` : ''}`;
  };
  const off = priceOf(p).off;
  $('#view').innerHTML = `<div class="pp">
    <div class="pp-gal">
      ${off ? `<span class="off">-${off}%</span>` : ''}
      <button class="heart ${S.wish.includes(p.handle) ? 'on' : ''}" data-wish="${esc(p.handle)}">${I.heart}</button>
      <div class="pp-track" id="ppTrack">${imgs.map(s => `<img src="${esc(s)}" alt="${esc(p.title)}" onerror="bcImgErr(this)">`).join('')}</div>
      ${imgs.length > 1 ? `<div class="hero-dots" id="ppDots">${imgs.map((_, i) => `<i class="${i ? '' : 'on'}"></i>`).join('')}</div>` : ''}
    </div>
    <div class="pp-body">
      <div class="pp-type">${esc(p.product_type || 'Baba Cosmetics')}</div>
      <h1>${esc(p.title)}</h1>
      <div class="pp-price" id="ppPrice">${priceHtml()}</div>
      <div class="pp-note">${I.truck}<span>${FREE_DELIVERY <= priceOf(p).price ? '<b>Free delivery</b> on this order!' : `Add items worth <b>Rs. 3,000</b> for <b>FREE delivery</b>`} • Cash on Delivery available</span></div>
      ${p.variants.length > 1 ? `<div class="pp-label">Choose option</div><div class="vars" id="ppVars">${p.variants.map((v, i) => `<button class="chip ${i === vi ? 'on' : ''}" data-v="${i}" ${v.available === false ? 'disabled style="opacity:.4"' : ''}>${esc(v.title)}</button>`).join('')}</div>` : ''}
      <div class="pp-label">Quantity</div>
      <div class="qty"><button data-q="-1">−</button><span id="ppQty">1</span><button data-q="1">+</button></div>
      ${desc ? `<details class="acc" open><summary>Product details ${I.down}</summary><div class="desc">${desc}</div></details>` : ''}
      <details class="acc"><summary>Delivery & Payment ${I.down}</summary><div class="desc">• Cash on Delivery all over Pakistan<br>• Free delivery on orders of Rs. 3,000 or more<br>• Delivered by PostEx / TCS courier<br>• Order help on WhatsApp: 0325-1370000</div></details>
    </div>
    <section class="sec">${secHead('You may also like')}${rail(more)}</section>
  </div>
  <div class="buybar">
    <button class="btn ghost" id="ppAdd">${I.bag} Add to Cart</button>
    <button class="btn" id="ppBuy">Buy Now</button>
  </div>`;
  const tr = $('#ppTrack'), dots = $$('#ppDots i');
  tr.addEventListener('scroll', () => { const i = Math.round(tr.scrollLeft / tr.clientWidth); dots.forEach((d, k) => d.classList.toggle('on', k === i)); }, { passive: true });
  const vars = $('#ppVars');
  if (vars) vars.addEventListener('click', e => { const b = e.target.closest('[data-v]'); if (!b || b.disabled) return; vi = +b.dataset.v; $$('#ppVars .chip').forEach(c => c.classList.toggle('on', c === b)); $('#ppPrice').innerHTML = priceHtml(); });
  $$('[data-q]').forEach(b => b.addEventListener('click', () => { qty = Math.max(1, Math.min(20, qty + +b.dataset.q)); $('#ppQty').textContent = qty; }));
  const sold = !available(p);
  if (sold) { $('#ppAdd').disabled = true; $('#ppBuy').disabled = true; $('#ppBuy').textContent = 'Sold out'; $('#ppAdd').style.opacity = $('#ppBuy').style.opacity = .5; }
  $('#ppAdd').onclick = () => addToCart(p, vi, qty);
  $('#ppBuy').onclick = () => { addToCart(p, vi, qty, true); location.hash = '#/cart'; };
};

V.search = function (q) {
  setTop({ title: 'Search' }); setTab('search');
  const init = decodeURIComponent(new URLSearchParams(q || '').get('q') || '');
  const popular = ['Vitamin C', 'Bleach', 'Serum', 'Face Wash', 'Sun Block', 'Parlor Pack', 'Urgent Facial', 'Men', 'Bundle', 'Coffee'];
  $('#view').innerHTML = `
    <div class="sbox">${I.search}<input id="sIn" type="search" placeholder="Search products…" value="${esc(init)}" autocomplete="off"><button class="mic" id="sMic">${I.mic}</button></div>
    <div id="sOut"></div>`;
  const out = $('#sOut'), inp = $('#sIn');
  const idle = () => {
    out.innerHTML = `${S.recent.length ? `<section class="sec">${secHead('Recent searches')}<div class="chips">${S.recent.map(r => `<button class="chip" data-term="${esc(r)}">${I.clock.replace('<svg', '<svg style="width:15px;height:15px;margin-right:5px"')}${esc(r)}</button>`).join('')}</div></section>` : ''}
      <section class="sec">${secHead('Popular searches')}<div class="chips">${popular.map(r => `<button class="chip" data-term="${esc(r)}">${esc(r)}</button>`).join('')}</div></section>
      <section class="sec">${secHead('Trending now')}<div class="pgrid">${S.products.slice(0, 6).map(card).join('')}</div></section>`;
  };
  const run = term => {
    const t = term.trim().toLowerCase();
    if (!t) return idle();
    const words = t.split(/\s+/);
    const res = S.products.map(p => {
      const hay = (p.title + ' ' + p.product_type + ' ' + (p.tags || []).join(' ')).toLowerCase();
      let s = 0; words.forEach(w => { if (hay.includes(w)) s += 2; else if (w.length > 3 && hay.includes(w.slice(0, -1))) s += 1; });
      if (p.title.toLowerCase().includes(t)) s += 5;
      return [s, p];
    }).filter(x => x[0] >= words.length).sort((a, b) => b[0] - a[0]).map(x => x[1]);
    out.innerHTML = `<section class="sec">${secHead(`${res.length} result${res.length === 1 ? '' : 's'} for "${esc(term)}"`)}<div class="pgrid">${res.map(card).join('')}</div>
      ${res.length ? '' : `<div class="empty" style="padding-top:20px"><div class="ico">${I.search}</div><h3>Kuch nahi mila</h3><p>Koi aur naam try karein, ya WhatsApp par poochein.</p></div>`}</section>`;
  };
  const remember = term => { term = term.trim(); if (!term) return; S.recent = [term].concat(S.recent.filter(r => r.toLowerCase() !== term.toLowerCase())).slice(0, 6); store.set('recent', S.recent); };
  let tm; inp.addEventListener('input', () => { clearTimeout(tm); tm = setTimeout(() => run(inp.value), 180); });
  inp.addEventListener('keydown', e => { if (e.key === 'Enter') { remember(inp.value); inp.blur(); } });
  out.addEventListener('click', e => { const c = e.target.closest('[data-term]'); if (c) { inp.value = c.dataset.term; remember(c.dataset.term); run(c.dataset.term); } });
  $('#sMic').onclick = () => voiceSearch();
  if (init) { run(init); remember(init); } else { idle(); setTimeout(() => inp.focus(), 250); }
};

V.wish = function () {
  setTop({ title: 'My Wishlist' }); setTab('wish');
  const L = S.wish.map(byHandle).filter(Boolean);
  $('#view').innerHTML = L.length
    ? `<div class="listhead"><h1>Wishlist</h1><p>${L.length} saved item${L.length > 1 ? 's' : ''}</p></div><div class="sec" style="padding-top:12px"><div class="pgrid">${L.map(card).join('')}</div></div>`
    : `<div class="empty"><div class="ico">${I.heart}</div><h3>Wishlist khaali hai</h3><p>Pasand ke products par ♥ dabayein, wo yahan save ho jayenge.</p><a class="btn" href="#/home">Start Shopping</a></div>`;
};

V.cart = function () {
  setTop({ back: true, title: 'My Cart' }); setTab('');
  const draw = () => {
    if (!S.cart.length) {
      $('#view').innerHTML = `<div class="empty"><div class="ico">${I.bag}</div><h3>Cart khaali hai</h3><p>Apni pasand ke products add karein.</p><a class="btn" href="#/home">Continue Shopping</a></div>`;
      return;
    }
    const total = cartTotal(), save = S.cart.reduce((a, i) => a + Math.max(0, i.cmp - i.price) * i.qty, 0);
    const left = Math.max(0, FREE_DELIVERY - total), pct = Math.min(100, total / FREE_DELIVERY * 100);
    $('#view').innerHTML = `
      <div class="fdbar"><p>${left ? `Sirf <b>${rs(left)}</b> aur — <b>FREE delivery</b> unlock karein! 🚚` : '🎉 Mubarak! Aap ko <b>FREE delivery</b> mil gayi.'}</p><div class="bar"><i style="width:${pct}%"></i></div></div>
      ${S.cart.map((i, n) => `<div class="citem">
        <a class="ci-img" href="#/p/${esc(i.handle)}"><img src="${esc(i.img)}" alt="${esc(i.title)}" onerror="bcImgErr(this)"></a>
        <div class="ci-b"><h4>${esc(i.title)}</h4>${i.vtitle ? `<div class="ci-v">${esc(i.vtitle)}</div>` : ''}
          <div class="price"><b>${rs(i.price)}</b>${i.cmp > i.price ? `<s>${rs(i.cmp)}</s>` : ''}</div>
          <div class="ci-row"><div class="qty"><button data-cq="${n}" data-d="-1">−</button><span>${i.qty}</span><button data-cq="${n}" data-d="1">+</button></div>
          <button class="ci-del" data-del="${n}" aria-label="Remove">${I.trash}</button></div></div></div>`).join('')}
      <div class="summary">
        <div><span>Subtotal</span><span>${rs(total)}</span></div>
        ${save ? `<div style="color:var(--ok)"><span>You save</span><span>− ${rs(save)}</span></div>` : ''}
        <div><span>Delivery</span><span>${left ? 'At checkout' : 'FREE'}</span></div>
        <div class="tot"><span>Total</span><span>${rs(total)}</span></div>
      </div>
      <div class="cartbtns">
        <button class="btn" id="goCheckout">Checkout • ${rs(total)}</button>
        <button class="btn wa" id="goWa">${I.wa} Order on WhatsApp</button>
      </div>`;
    $('#goCheckout').onclick = checkout;
    $('#goWa').onclick = whatsappOrder;
  };
  draw();
  $('#view').onclick = e => {
    const q = e.target.closest('[data-cq]'), d = e.target.closest('[data-del]');
    if (q) { const it = S.cart[+q.dataset.cq]; it.qty = Math.max(1, Math.min(50, it.qty + +q.dataset.d)); saveCart(); draw(); }
    if (d) { S.cart.splice(+d.dataset.del, 1); saveCart(); draw(); toast('Item removed'); }
  };
};

V.account = function () {
  setTop({ title: 'Account' }); setTab('account');
  const row = (ico, t, s, attr) => `<button ${attr}><span class="mi">${ico}</span><span>${t}<small>${s}</small></span>${I.chev.replace('<svg', '<svg class="chev"')}</button>`;
  $('#view').innerHTML = `
    <div class="acc-hero"><h2>Assalam o Alaikum! 👋</h2><p>Baba Cosmetics app mein khush aamdeed</p></div>
    <div class="menu-list">
      ${row(I.box, 'My Orders', 'Track & view your orders', `data-ext="${STORE}/account"`)}
      ${row(I.heart, 'My Wishlist', S.wish.length + ' saved items', 'data-go="#/wish"')}
      ${row(I.bag, 'My Cart', S.cart.length + ' items', 'data-go="#/cart"')}
    </div>
    <div class="menu-list">
      ${row(I.wa, 'WhatsApp Help', '0325-1370000', `data-ext="https://wa.me/${WHATSAPP}"`)}
      ${row(I.mail, 'Email Us', 'info@babacosmetics.com', 'data-ext="mailto:info@babacosmetics.com"')}
      ${row(I.globe, 'Visit Website', 'www.babacosmetics.com', `data-ext="${STORE}"`)}
      ${row(I.pin, 'Head Office', 'Plot 158, Ata Turk Block, Garden Town, Lahore', 'data-none')}
    </div>
    <div class="menu-list">
      ${row(I.truck, 'Delivery Info', 'Free delivery on Rs. 3,000+ • COD', 'data-go="#/all"')}
      ${row(I.shield, 'Privacy Policy', 'How we use your data', `data-ext="${STORE}/policies/privacy-policy"`)}
      ${row(I.box, 'Refund Policy', 'Returns & exchanges', `data-ext="${STORE}/policies/refund-policy"`)}
    </div>
    <div class="foot">App version 2.0 ${S.live ? '• Live store' : '• Offline mode'}</div>`;
};

/* =========================================================
   DRAWER
   ========================================================= */
function buildDrawer() {
  const cats = liveCats();
  $('#drList').innerHTML = `
    <a href="#/home"><span class="dot">${I.spark}</span>Home</a>
    <a href="#/all"><span class="dot">${I.grid}</span>All Products</a>
    <a href="#/all?sort=new"><span class="dot">${I.spark}</span>New Arrivals</a>
    <a href="#/all?sort=off"><span class="dot">${I.cash}</span>Best Deals</a>
    <div class="dr-cap">Shop by category</div>
    ${cats.map(c => { const p = localCat(c)[0]; return `<a href="#/c/${c.h}">${p ? `<img src="${esc(firstImg(p, 120))}" alt="" onerror="this.style.visibility='hidden'">` : `<span class="dot">${I.spark}</span>`}${c.n}</a>`; }).join('')}
    <hr>
    <a href="#" data-ext="https://wa.me/${WHATSAPP}"><span class="dot">${I.wa}</span>WhatsApp Order</a>
    <a href="#" data-ext="${STORE}"><span class="dot">${I.globe}</span>Website</a>`;
}
function drawer(on) { $('#drawer').classList.toggle('on', on); $('#drawerMask').classList.toggle('on', on); }

/* =========================================================
   VOICE SEARCH
   ========================================================= */
async function voiceSearch() {
  const go = text => { if (text) location.hash = '#/search?q=' + encodeURIComponent(text); };
  const SR = Plug('SpeechRecognition');
  if (SR) {
    try {
      const av = await SR.available();
      if (!av.available) { toast('Voice search is phone par available nahi'); return; }
      let perm = await SR.checkPermissions();
      if (perm.speechRecognition !== 'granted') perm = await SR.requestPermissions();
      if (perm.speechRecognition !== 'granted') { toast('Microphone permission chahiye'); return; }
      const r = await SR.start({ language: 'en-US', maxResults: 3, prompt: 'Product ka naam bolein…', partialResults: false, popup: true });
      go(r && r.matches && r.matches[0]);
    } catch (e) { toast('Awaaz samajh nahi aayi, dobara try karein'); }
    return;
  }
  const W = window.SpeechRecognition || window.webkitSpeechRecognition;
  if (!W) { toast('Voice search is device par supported nahi'); return; }
  const rec = new W(); rec.lang = 'en-US'; rec.interimResults = true;
  $('#voice').classList.remove('hidden'); $('#voTitle').textContent = 'Listening…';
  let final = '';
  rec.onresult = e => { final = Array.from(e.results).map(r => r[0].transcript).join(' '); $('#voText').textContent = '"' + final + '"'; };
  rec.onend = () => { $('#voice').classList.add('hidden'); go(final); };
  rec.onerror = () => { $('#voice').classList.add('hidden'); };
  $('#voCancel').onclick = () => { final = ''; rec.abort(); $('#voice').classList.add('hidden'); };
  rec.start();
}

/* =========================================================
   ROUTER
   ========================================================= */
function route() {
  clearInterval(heroTimer); clearInterval(hintTimer);
  drawer(false);
  $('#view').onclick = null;
  const h = location.hash.replace(/^#\/?/, '') || 'home';
  const [pathQ, ...rest] = [h];
  const [path, query] = pathQ.split('?');
  const [name, arg] = [path.split('/')[0], path.split('/').slice(1).join('/')];
  const view = $('#view');
  view.style.animation = 'none'; void view.offsetWidth; view.style.animation = '';
  window.scrollTo(0, 0);
  document.body.classList.toggle('pp-mode', name === 'p');
  if (name === 'p') V.p(decodeURIComponent(arg));
  else if (name === 'c') V.c(decodeURIComponent(arg));
  else if (name === 'cat') V.cat(arg);
  else if (name === 'all') V.all(query);
  else if (name === 'search') V.search(query);
  else if (V[name]) V[name]();
  else V.home();
}

/* =========================================================
   EVENTS
   ========================================================= */
document.addEventListener('click', e => {
  const w = e.target.closest('[data-wish]');
  if (w) { e.preventDefault(); e.stopPropagation(); toggleWish(w.dataset.wish, w); return; }
  const a = e.target.closest('[data-add]');
  if (a) { e.preventDefault(); e.stopPropagation(); const p = byHandle(a.dataset.add); if (p) addToCart(p); return; }
  const x = e.target.closest('[data-ext]');
  if (x) { e.preventDefault(); openExternal(x.dataset.ext); return; }
  const g = e.target.closest('[data-go]');
  if (g) { location.hash = g.dataset.go; return; }
  const c = e.target.closest('.pcard[data-p]');
  if (c) { location.hash = '#/p/' + encodeURIComponent(c.dataset.p); }
});
$('#btnMenu').onclick = () => drawer(true);
$('#drClose').onclick = () => drawer(false);
$('#drawerMask').onclick = () => drawer(false);
$('#drList').addEventListener('click', e => { if (e.target.closest('a')) setTimeout(() => drawer(false), 50); });
$('#btnBack').onclick = () => { if (history.length > 1) history.back(); else location.hash = '#/home'; };
$('#btnCart').onclick = () => { location.hash = '#/cart'; };
$('#searchPill').onclick = e => { if (e.target.closest('#micTop')) { voiceSearch(); return; } location.hash = '#/search'; };
window.addEventListener('scroll', () => $('#topbar').classList.toggle('shadow', scrollY > 8), { passive: true });
window.addEventListener('hashchange', route);

// Android hardware back button
const AppP = Plug('App');
if (AppP) AppP.addListener('backButton', () => {
  if ($('#drawer').classList.contains('on')) return drawer(false);
  if (!$('#voice').classList.contains('hidden')) return $('#voice').classList.add('hidden');
  const h = location.hash.replace(/^#\/?/, '');
  if (!h || h === 'home') AppP.exitApp(); else history.back();
});

/* =========================================================
   BOOT: splash -> loading -> app
   ========================================================= */
(async function boot() {
  setLogo($('#spLogo'), $('#spWord'));
  setLogo($('.tb-logo'), $('.tb-word'));
  $$('.dr-logo').forEach(i => setLogo(i, $('.dr-word')));
  try { const SB = Plug('StatusBar'); if (SB) { SB.setBackgroundColor({ color: '#FFFFFF' }); SB.setStyle({ style: 'LIGHT' }); } } catch (e) {}
  updBadge();
  const minSplash = new Promise(r => setTimeout(r, 2600));
  const data = S.products.length
    ? (loadData().then(() => { buildDrawer(); }), Promise.resolve())   // cached: start fast, refresh in background
    : Promise.race([loadData(), new Promise(r => setTimeout(r, 9000))]);
  await Promise.all([minSplash, data]);
  if (!S.products.length) S.products = (window.BC_SEED && window.BC_SEED.products) || [];
  buildDrawer();
  $('#app').classList.remove('hidden');
  route();
  $('#splash').classList.add('out');
  setTimeout(() => $('#splash').remove(), 700);
})();

})();
