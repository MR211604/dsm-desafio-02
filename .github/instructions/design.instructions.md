---
applyTo: "**"
---

# Design System Strategy: The Elevated Voyager

This design system translates the utility of native Android Material Design into a high-end, editorial travel experience. It moves away from the "utility-first" look of standard apps toward a "Digital Concierge" aesthetic—prioritizing depth, tonal layering, and sophisticated typography to inspire wanderlust while maintaining professional reliability.

---

### 1. Creative North Star: The Digital Concierge

The "Digital Concierge" philosophy dictates that the UI should never feel like a spreadsheet of data. Instead, it should feel like a curated travel journal. We achieve this by breaking the rigid Material grid with **intentional asymmetry** and **tonal depth**.

By utilizing the "Soft Blue" (`5F70E4`) and "Light Lavender" (`D1C4E9`) palettes, we create a calm, trustworthy environment. We reject the "flat" look of early Material Design in favor of a "Physical Layering" approach, where information is presented on "sheets" of color rather than separated by harsh lines.

---

### 2. Colors & Tonal Architecture

Color in this system is not just decorative; it defines the architecture of the screen.

#### The "No-Line" Rule

Standard 1px borders are strictly prohibited for sectioning. Structural boundaries must be defined solely through background color shifts.

- **Implementation:** Place a `surface_container_low` section atop a `surface` background. The shift in tone provides a sophisticated boundary that feels organic rather than mechanical.

#### Surface Hierarchy & Nesting

Treat the UI as a series of nested physical layers.

- **Base Layer:** `surface` (fbf8ff)
- **Secondary Sections:** `surface_container_low` (f4f2fd)
- **Interactive Cards:** `surface_container_highest` (e3e1eb)
- **Inner Content Wells:** Use `surface_container_lowest` (ffffff) to make high-priority content (like flight details) "pop" with a clean, crisp white.

#### The "Glass & Gradient" Rule

To elevate the travel experience, use Glassmorphism for floating headers and Navigation Bars.

- **Token:** Use `surface` at 85% opacity with a `20dp` backdrop blur.
- **Signature Textures:** For Hero CTAs (e.g., "Book Now"), apply a subtle linear gradient from `primary` (3e50c3) to `primary_container` (596add) at a 135-degree angle. This adds "soul" and a tactile quality that flat colors lacks.

---

### 3. Typography: Editorial Authority

We utilize a pairing of **Plus Jakarta Sans** for expressive moments and **Inter** for high-utility data.

- **Display (Plus Jakarta Sans):** Used for destination names and inspirational headlines. The `display-lg` (3.5rem) should be used with tight letter-spacing to create an authoritative, editorial look.
- **Title & Body (Inter):** Used for all functional data. `title-md` (1.125rem) is the workhorse for card titles, providing a modern, "tech-forward" legibility.
- **The Hierarchy Strategy:** Large `headline-lg` titles should often be paired with a much smaller `label-md` uppercase subtitle. This high contrast in scale mimics luxury travel magazines and directs the eye immediately to the most important information.

---

### 4. Elevation & Depth: Tonal Layering

We replace traditional structural lines with the **Layering Principle**.

- **Ambient Shadows:** For the Floating Action Button (FAB) or high-priority `CardView`, do not use pure black shadows. Use a shadow color tinted with `on_surface` (1a1b22) at a 6% opacity with a blur radius of `16dp`. This mimics natural, ambient light.
- **Ghost Borders:** If a border is required for accessibility in input fields, use the `outline_variant` token at 20% opacity. Never use 100% opaque borders; they clutter the visual field.
- **Depth through Blur:** Use `surface_tint` semi-transparently over imagery to create "scrims" for text overlays, ensuring the "Digital Concierge" remains readable over busy travel photography.

---

### 5. Component Guidelines

#### Cards & RecyclerView

- **Rule:** Forbid the use of `Divider` lines.
- **Style:** Use `surface_container_high` for card backgrounds.
- **Spacing:** Use `spacing-6` (1.5rem) between cards to allow the background color to act as a natural separator.
- **Corner Radius:** Cards must use `roundedness-lg` (1rem) for a friendly, premium feel.

#### Buttons (The Travel CTA)

- **Primary:** Gradient fill (`primary` to `primary_container`). `roundedness-full` (capsule shape).
- **Secondary:** `surface_container_highest` background with `on_surface` text. No border.
- **Tertiary:** Transparent background with `primary` text. Use for low-emphasis actions like "View Map."

#### Floating Action Button (FAB)

- The FAB should utilize the `primary` color. In a travel context, the FAB is the "Primary Action" (e.g., "Start Booking"). Use a `primary_fixed_dim` shadow to give it a soft, glowing lift.

#### Input Fields

- **Style:** Filled style using `surface_container_low`.
- **Indicator:** A 2dp bottom stroke using `primary` only when focused. Otherwise, no visible border. This keeps the forms looking "clean" and less intimidating for complex booking flows.

#### Search & Filters (Chips)

- **Filter Chips:** Use `secondary_container` (e4d7fd) for unselected states and `primary` for selected states. This introduces the "Light Lavender" secondary color to denote personalization and user choice.

---

### 6. Do’s and Don’ts

**Do:**

- **Do** use asymmetrical padding (e.g., `spacing-8` on the top and `spacing-4` on the bottom) to create a dynamic, editorial rhythm.
- **Do** use `surface_bright` for screen transitions to keep the app feeling airy and fast.
- **Do** leverage imagery. This system is designed to "frame" high-quality travel photography, not compete with it.

**Don’t:**

- **Don’t** use standard Android `CCCCCC` dividers. Use whitespace or a `surface_container` shift instead.
- **Don’t** use hard-edged rectangles. Everything should have a minimum of `roundedness-md`.
- **Don’t** use high-contrast black text on pure white. Stick to `on_surface` (1a1b22) on `surface` variants for a softer, premium reading experience.
